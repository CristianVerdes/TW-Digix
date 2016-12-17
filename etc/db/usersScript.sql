DROP TRIGGER USERS_INCREMENT;
DROP SEQUENCE USERS_SEQ;
DROP SEQUENCE CONTENTS_SEQ;
DROP SEQUENCE FRIENDS_SEQ;

DROP TABLE DIGIX.USERCONNECTION;
DROP TABLE DIGIX.USER_PROFILES;
DROP TABLE DIGIX.TAGS;
DROP TABLE DIGIX.LOCATIONS;
DROP TABLE DIGIX.CONTENTS;
DROP TABLE DIGIX.FRIENDS;
DROP TABLE DIGIX.USERS;
DROP TABLE DIGIX.CONTENT_TYPES;

DROP PACKAGE USER_UTILITY;
DROP PACKAGE USER_EXCEPTIONS;
DROP PACKAGE CONTENT_UTILITY;
DROP PACKAGE CONTENT_EXCEPTIONS;
DROP PACKAGE SEARCH_UTILITY;
DROP PACKAGE DB_UTILITY;

DROP TYPE CONTENT_OBJECTS;
DROP TYPE CONTENT_OBJECT;


CREATE TABLE USERS
(
    USER_ID     NUMBER(10),
    EMAIL       VARCHAR2(60) UNIQUE,
    PASSWORD    VARCHAR2(40),
    FIRST_NAME  VARCHAR2(40),
    LAST_NAME   VARCHAR2(40),
    STATUS      VARCHAR2(10),
    ACCESSTOKEN VARCHAR2(40),

    CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);

CREATE SEQUENCE USERS_SEQ;

CREATE OR REPLACE TRIGGER USERS_INCREMENT
BEFORE INSERT ON USERS
FOR EACH ROW

    BEGIN
        SELECT USERS_SEQ.NEXTVAL
        INTO :NEW.USER_ID
        FROM DUAL;
    END;

CREATE PACKAGE USER_EXCEPTIONS IS
        USER_NOT_FOUND EXCEPTION;
        USER_FOUND EXCEPTION;
END USER_EXCEPTIONS;

CREATE PACKAGE BODY USER_EXCEPTIONS IS

        USER_NOT_FOUND EXCEPTION;
PRAGMA EXCEPTION_INIT (USER_NOT_FOUND, -20001);

        USER_FOUND EXCEPTION;
PRAGMA EXCEPTION_INIT (USER_FOUND, -20002);

END USER_EXCEPTIONS;


CREATE PACKAGE USER_UTILITY IS

    FUNCTION GET_HASH(P_USERNAME IN VARCHAR2,
                      P_PASSWORD IN VARCHAR2)
        RETURN VARCHAR2;

    FUNCTION GET_ACCESSTOKEN(P_EMAIL IN VARCHAR2, P_PASSWORD IN VARCHAR2)
        RETURN VARCHAR2;

    PROCEDURE LOG_IN(P_EMAIL    IN USERS.EMAIL%TYPE,
                     P_PASSWORD IN USERS.PASSWORD%TYPE);

    PROCEDURE REGISTER(P_EMAIL      IN USERS.EMAIL%TYPE,
                       P_PASSWORD   IN USERS.PASSWORD%TYPE,
                       P_FIRST_NAME IN USERS.FIRST_NAME%TYPE,
                       P_LAST_NAME  IN USERS.LAST_NAME%TYPE);

    PROCEDURE LOG_OUT(P_EMAIL IN USERS.EMAIL%TYPE);

END USER_UTILITY;

CREATE PACKAGE BODY USER_UTILITY IS
    COUNTER NUMBER;

    FUNCTION GET_HASH(P_USERNAME IN VARCHAR2,
                      P_PASSWORD IN VARCHAR2)
        RETURN VARCHAR2 AS
        l_salt VARCHAR2(30) := 'digix';
        BEGIN
            -- Oracle 10g+ : Requires EXECUTE on DBMS_CRYPTO
            RETURN DBMS_CRYPTO.HASH(UTL_RAW.CAST_TO_RAW(l_salt || UPPER(P_PASSWORD)),
                                    DBMS_CRYPTO.HASH_SH1);
        END GET_HASH;

    FUNCTION GET_ACCESSTOKEN(P_EMAIL IN VARCHAR2, P_PASSWORD IN VARCHAR2)
        RETURN VARCHAR2 AS
        l_salt VARCHAR2(30) := 'digix';
        BEGIN
            RETURN DBMS_CRYPTO.HASH(UTL_RAW.CAST_TO_RAW(UPPER(P_EMAIL) || l_salt || UPPER(P_PASSWORD)),
                                    DBMS_CRYPTO.HASH_SH1);
        END GET_ACCESSTOKEN;

    PROCEDURE LOG_IN(P_EMAIL    IN USERS.EMAIL%TYPE,
                     P_PASSWORD IN USERS.PASSWORD%TYPE) IS
        BEGIN
            COUNTER := 0;
            SELECT COUNT(*)
            INTO COUNTER
            FROM USERS
            WHERE EMAIL = p_email AND
                  PASSWORD = GET_HASH(p_email, p_password);
            IF COUNTER = 0
            THEN
                RAISE USER_EXCEPTIONS.USER_NOT_FOUND;
            ELSE
                UPDATE USERS
                SET STATUS = 'ONLINE'
                WHERE EMAIL = p_email;
            END IF;

            EXCEPTION
            WHEN USER_EXCEPTIONS.USER_NOT_FOUND THEN
            raise_application_error(-20001, 'User not found');
        END LOG_IN;

    PROCEDURE REGISTER(P_EMAIL      IN USERS.EMAIL%TYPE,
                       P_PASSWORD   IN USERS.PASSWORD%TYPE,
                       P_FIRST_NAME IN USERS.FIRST_NAME%TYPE,
                       P_LAST_NAME  IN USERS.LAST_NAME%TYPE) IS
        BEGIN
            COUNTER := 0;
            SELECT COUNT(*)
            INTO COUNTER
            FROM USERS
            WHERE EMAIL = P_EMAIL;

            IF COUNTER = 1
            THEN
                RAISE USER_EXCEPTIONS.USER_FOUND;
            ELSE
                INSERT INTO USERS (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, STATUS, ACCESSTOKEN)
                VALUES (P_EMAIL, GET_HASH(P_EMAIL, P_PASSWORD), P_FIRST_NAME, P_LAST_NAME, 'OFFLINE',
                        GET_ACCESSTOKEN(P_EMAIL, P_PASSWORD));
            END IF;

            EXCEPTION
            WHEN USER_EXCEPTIONS.USER_FOUND THEN
            raise_application_error(-20002, 'User found');

        END REGISTER;

    PROCEDURE LOG_OUT(P_EMAIL IN USERS.EMAIL%TYPE) IS
        BEGIN
            UPDATE USERS
            SET STATUS = 'OFFLINE'
            WHERE EMAIL = P_EMAIL;
        END LOG_OUT;

END;

CREATE TABLE USERCONNECTION (
    userId         VARCHAR(255) NOT NULL,
    providerId     VARCHAR(255) NOT NULL,
    providerUserId VARCHAR(255),
    rank           INT          NOT NULL,
    displayName    VARCHAR(255),
    profileUrl     VARCHAR(512),
    imageUrl       VARCHAR(512),
    accessToken    VARCHAR(512) NOT NULL,
    secret         VARCHAR(512),
    refreshToken   VARCHAR(512),
    expireTime     INTEGER,
    CONSTRAINT USERCONNECTION_PF PRIMARY KEY (userId, providerId, providerUserId),
    CONSTRAINT USERCONNECTION_FK FOREIGN KEY (userId) REFERENCES USERS (EMAIL) ON DELETE CASCADE
);

CREATE TABLE USER_PROFILES (
    USER_ID     NUMBER(10),
    JOIN_DATE   DATE,
    AVATAR_PATH VARCHAR2(320),
    BIRTHDAY    VARCHAR2(512),
    GENDER      VARCHAR2(512),
    LOCATION    VARCHAR2(512),
    CONSTRAINT USER_PROFILES_FK FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);

CREATE OR REPLACE TRIGGER CREATE_USER_PROFILE
AFTER INSERT
ON USERS
FOR EACH ROW
    DECLARE
        USER_ID   NUMBER(10);
        JOIN_DATE DATE;
        STATEMENT VARCHAR2(320);
    BEGIN
        USER_ID := :NEW.USER_ID;
        SELECT SYSDATE
        INTO JOIN_DATE
        FROM DUAL;

        STATEMENT := 'INSERT INTO USER_PROFILES(USER_ID, JOIN_DATE) VALUES (:a, :b)';
        EXECUTE IMMEDIATE STATEMENT USING USER_ID, JOIN_DATE;

    END;

CREATE TABLE CONTENT_TYPES (
    TYPE_ID NUMBER(10),
    TYPE    VARCHAR2(20),

    CONSTRAINT CONTENT_TYPES_PK PRIMARY KEY (TYPE_ID)
);

INSERT INTO CONTENT_TYPES VALUES (1, 'Photo');
INSERT INTO CONTENT_TYPES VALUES (2, 'Video');
INSERT INTO CONTENT_TYPES VALUES (3, 'Document');


CREATE TABLE CONTENTS (
    CONTENT_ID     NUMBER(10),
    USER_ID        NUMBER(10) NOT NULL,
    TYPE_ID        NUMBER(10),
    PROVIDER       VARCHAR2(255),
    PATH           VARCHAR2(512),
    THUMBNAIL_LINK VARCHAR2(512),
    DESCRIPTION    VARCHAR2(1024),
    POSTED_DATE    DATE,
    UPLOADED_DATE  DATE,
    PROVIDER_ID    VARCHAR2(255),

    CONSTRAINT CONTENTS_PK PRIMARY KEY (CONTENT_ID),
    CONSTRAINT CONTENTS_FK1 FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    CONSTRAINT CONTENTS_FK2 FOREIGN KEY (TYPE_ID) REFERENCES CONTENT_TYPES (TYPE_ID) ON DELETE CASCADE
);

CREATE TABLE LOCATIONS
(
    CONTENT_ID NUMBER(10),
    COUNTRY    VARCHAR2(3200),
    CITY       VARCHAR2(3200),
    STREET     VARCHAR2(3200),
    LATITUDE   VARCHAR2(3200),
    LONGITUDE  VARCHAR2(3200),

    CONSTRAINT LOCATIONS_FK FOREIGN KEY (CONTENT_ID) REFERENCES CONTENTS (CONTENT_ID) ON DELETE CASCADE
);

CREATE TABLE FRIENDS (
    FRIEND_ID    NUMBER(10),
    PROVIDER_ID  VARCHAR2(255),
    USER_ID      NUMBER(10) NOT NULL,
    NAME         VARCHAR2(255),
    RELATIONSHIP VARCHAR2(128),
    CONSTRAINT FRIENDS_PK PRIMARY KEY (FRIEND_ID),
    CONSTRAINT FRIENDS_FK FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);

CREATE SEQUENCE FRIENDS_SEQ;

CREATE OR REPLACE TRIGGER FRIENDS_INCREMENT
BEFORE INSERT ON FRIENDS
FOR EACH ROW
    BEGIN
        SELECT FRIENDS_SEQ.NEXTVAL
        INTO :NEW.FRIEND_ID
        FROM DUAL;
    END;

CREATE TABLE TAGS (
    CONTENT_ID NUMBER(10),
    FRIEND_ID  NUMBER(10),

    CONSTRAINT TAGS_FK1 FOREIGN KEY (CONTENT_ID) REFERENCES CONTENTS (CONTENT_ID) ON DELETE CASCADE,
    CONSTRAINT TAGS_FK2 FOREIGN KEY (FRIEND_ID) REFERENCES FRIENDS (FRIEND_ID) ON DELETE CASCADE
);


CREATE SEQUENCE CONTENTS_SEQ;

CREATE OR REPLACE TRIGGER CONTENTS_INCREMENT
BEFORE INSERT ON CONTENTS
FOR EACH ROW
    BEGIN
        SELECT CONTENTS_SEQ.NEXTVAL
        INTO :NEW.CONTENT_ID
        FROM DUAL;
    END;

CREATE TYPE CONTENT_OBJECT AS OBJECT (
    CONTENT_ID  NUMBER(10),
    USER_ID     NUMBER(10),
    TYPE_ID     NUMBER(10),
    PROVIDER    VARCHAR2(255),
    PATH        VARCHAR2(512),
    DESCRIPTION VARCHAR2(512),
    POSTED_DATE DATE,
    PROVIDER_ID VARCHAR2(255)
);

CREATE TYPE CONTENT_OBJECTS IS TABLE OF CONTENT_OBJECT;


CREATE PACKAGE CONTENT_EXCEPTIONS IS
        CONTENT_NOT_FOUND EXCEPTION;
END CONTENT_EXCEPTIONS;

CREATE PACKAGE BODY CONTENT_EXCEPTIONS IS

        CONTENT_NOT_FOUND EXCEPTION;
PRAGMA EXCEPTION_INIT (CONTENT_NOT_FOUND, -20003);

END CONTENT_EXCEPTIONS;

CREATE PACKAGE CONTENT_UTILITY IS

    PROCEDURE ADD_CONTENT(P_USER_ID        IN CONTENTS.USER_ID%TYPE,
                          P_TYPE_ID        IN CONTENTS.TYPE_ID%TYPE,
                          P_PROVIDER       IN CONTENTS.PROVIDER%TYPE,
                          P_PATH           IN CONTENTS.PATH%TYPE,
                          P_THUMBNAIL_LINK IN CONTENTS.THUMBNAIL_LINK%TYPE,
                          P_DESCRIPTION    IN CONTENTS.DESCRIPTION%TYPE,
                          P_PROVIDER_ID    IN CONTENTS.PROVIDER_ID%TYPE,
                          P_UPLOADED_DATE  IN CONTENTS.UPLOADED_DATE%TYPE);

    PROCEDURE POST_CONTENT(P_CONTENT_ID IN CONTENTS.CONTENT_ID%TYPE);

    PROCEDURE GET_CONTENT(P_CONTENT_ID IN CONTENTS.CONTENT_ID%TYPE, content OUT CONTENT_OBJECT);

    PROCEDURE GET_PHOTOS(P_USER_ID IN CONTENTS.USER_ID%TYPE, photos OUT CONTENT_OBJECTS);
    PROCEDURE GET_VIDEOS(P_USER_ID IN CONTENTS.USER_ID%TYPE, videos OUT CONTENT_OBJECTS);

END CONTENT_UTILITY;

CREATE PACKAGE BODY CONTENT_UTILITY IS

    PROCEDURE ADD_CONTENT(P_USER_ID        IN CONTENTS.USER_ID%TYPE,
                          P_TYPE_ID        IN CONTENTS.TYPE_ID%TYPE,
                          P_PROVIDER       IN CONTENTS.PROVIDER%TYPE,
                          P_PATH           IN CONTENTS.PATH%TYPE,
                          P_THUMBNAIL_LINK IN CONTENTS.THUMBNAIL_LINK%TYPE,
                          P_DESCRIPTION    IN CONTENTS.DESCRIPTION%TYPE,
                          P_PROVIDER_ID    IN CONTENTS.PROVIDER_ID%TYPE,
                          P_UPLOADED_DATE  IN CONTENTS.UPLOADED_DATE%TYPE
    ) IS
        BEGIN
            INSERT INTO CONTENTS (USER_ID, TYPE_ID, PROVIDER, PATH, THUMBNAIL_LINK, DESCRIPTION, PROVIDER_ID, UPLOADED_DATE)
            VALUES (P_USER_ID, P_TYPE_ID, P_PROVIDER, P_PATH, P_THUMBNAIL_LINK, P_DESCRIPTION, P_PROVIDER_ID,
                    P_UPLOADED_DATE);
        END ADD_CONTENT;

    PROCEDURE POST_CONTENT(P_CONTENT_ID IN CONTENTS.CONTENT_ID%TYPE) IS
        V_POSTED_DATE DATE;
        BEGIN
            SELECT SYSDATE
            INTO V_POSTED_DATE
            FROM DUAL;
            UPDATE CONTENTS
            SET POSTED_DATE = V_POSTED_DATE
            WHERE CONTENT_ID = P_CONTENT_ID;
        END POST_CONTENT;

    PROCEDURE GET_CONTENT(P_CONTENT_ID IN CONTENTS.CONTENT_ID%TYPE, content OUT CONTENT_OBJECT) IS
        row_content CONTENTS%ROWTYPE;
        BEGIN
            SELECT *
            INTO row_content
            FROM CONTENTS
            WHERE CONTENT_ID = P_CONTENT_ID;
            content := CONTENT_OBJECT(row_content.CONTENT_ID, row_content.USER_ID, row_content.TYPE_ID,
                                      row_content.PROVIDER, row_content.PATH,
                                      row_content.DESCRIPTION, row_content.POSTED_DATE, row_content.PROVIDER_ID);
        END;


    PROCEDURE GET_PHOTOS(P_USER_ID IN CONTENTS.USER_ID%TYPE, photos OUT CONTENT_OBJECTS) IS
        COUNTER NUMBER(10);
        index1  NUMBER(10);
        BEGIN
            photos := CONTENT_OBJECTS();
            SELECT COUNT(*)
            INTO COUNTER
            FROM CONTENTS
            WHERE USER_ID = P_USER_ID AND TYPE_ID = 1;
            IF COUNTER = 0
            THEN
                RAISE CONTENT_EXCEPTIONS.CONTENT_NOT_FOUND;
            ELSE
                photos.extend(COUNTER);

                index1 := 1;
                FOR photo IN (SELECT *
                              FROM CONTENTS
                              WHERE USER_ID = P_USER_ID AND TYPE_ID = 1
                              ORDER BY CONTENT_ID DESC ) LOOP
                    photos(index1) := CONTENT_OBJECT(photo.CONTENT_ID, photo.USER_ID, photo.TYPE_ID, photo.PROVIDER,
                                                     photo.PATH,
                                                     photo.DESCRIPTION, photo.POSTED_DATE, photo.PROVIDER_ID);
                    index1 := index1 + 1;
                END LOOP;
            END IF;

            EXCEPTION
            WHEN CONTENT_EXCEPTIONS.CONTENT_NOT_FOUND THEN
            raise_application_error(-20003, 'Content not found');

        END GET_PHOTOS;

    PROCEDURE GET_VIDEOS(P_USER_ID IN CONTENTS.USER_ID%TYPE, videos OUT CONTENT_OBJECTS) IS
        COUNTER NUMBER(10);
        index2  NUMBER(10);
        BEGIN
            videos := CONTENT_OBJECTS();
            SELECT COUNT(*)
            INTO COUNTER
            FROM CONTENTS
            WHERE USER_ID = P_USER_ID AND TYPE_ID = 2;
            IF COUNTER = 0
            THEN
                RAISE CONTENT_EXCEPTIONS.CONTENT_NOT_FOUND;
            ELSE
                videos.extend(COUNTER);

                index2 := 1;
                FOR video IN (SELECT *
                              FROM CONTENTS
                              WHERE USER_ID = P_USER_ID AND TYPE_ID = 2
                              ORDER BY CONTENT_ID DESC ) LOOP
                    videos(index2) := CONTENT_OBJECT(video.CONTENT_ID, video.USER_ID, video.TYPE_ID, video.PROVIDER,
                                                     video.PATH,
                                                     video.DESCRIPTION, video.POSTED_DATE, video.PROVIDER_ID);
                    index2 := index2 + 1;
                END LOOP;
            END IF;

            EXCEPTION
            WHEN CONTENT_EXCEPTIONS.CONTENT_NOT_FOUND THEN
            raise_application_error(-20003, 'Content not found');

        END GET_VIDEOS;

END CONTENT_UTILITY;

CREATE PACKAGE SEARCH_UTILITY IS

    PROCEDURE SEARCH_CONTENT(
        P_TYPE_ID   IN  CONTENTS.TYPE_ID%TYPE,
        P_NAME      IN  USERS.FIRST_NAME%TYPE,
        P_YEAR      IN  VARCHAR2,
        P_LOCATION  IN  VARCHAR2,
        P_STATEMENT OUT VARCHAR2);

END SEARCH_UTILITY;

CREATE PACKAGE BODY SEARCH_UTILITY IS

    PROCEDURE SEARCH_CONTENT(
        P_TYPE_ID   IN  CONTENTS.TYPE_ID%TYPE,
        P_NAME      IN  USERS.FIRST_NAME%TYPE,
        P_YEAR      IN  VARCHAR2,
        P_LOCATION  IN  VARCHAR2,
        P_STATEMENT OUT VARCHAR2) IS

        whereClause VARCHAR2(3200);
        BEGIN

            P_STATEMENT := 'SELECT * FROM CONTENTS c FULL OUTER JOIN USERS U ON u.USER_ID = c.USER_ID ';
            whereClause := 'WHERE ';
            IF P_LOCATION IS NOT NULL
            THEN
                P_STATEMENT := P_STATEMENT || 'FULL OUTER JOIN LOCATIONS loc ON loc.CONTENT_ID = c.CONTENT_ID ';
                whereClause := whereClause || '(
                    LOWER(loc.CITY) = LOWER(' || CHR(39) || P_LOCATION || CHR(39) ||
                               ') OR LOWER(loc.COUNTRY) = LOWER(' || CHR(39) || P_LOCATION || CHR(39) || ')) AND ';
            END IF;
            IF P_NAME IS NOT NULL
            THEN
                P_STATEMENT := P_STATEMENT || 'JOIN USERS u ON u.USER_ID = c.USER_ID ';
                whereClause := whereClause || '(LOWER(u.FIRST_NAME) = LOWER(' || CHR(39) || P_NAME || CHR(39) ||
                               ') OR LOWER(u.LAST_NAME) = LOWER(' || CHR(39) || P_NAME || CHR(39) || ')) AND ';
            END IF;
            IF P_TYPE_ID IS NOT NULL
            THEN
                whereClause := whereClause || 'c.TYPE_ID = ' || CHR(39) || P_TYPE_ID || CHR(39) || ' AND ';
            END IF;
            IF P_YEAR IS NOT NULL
            THEN
                whereClause := whereClause || 'EXTRACT(YEAR FROM c.UPLOADED_DATE) = ' || CHR(39) || P_YEAR || CHR(39) ||
                               ' AND ';
            END IF;

            P_STATEMENT := P_STATEMENT || whereClause;

            P_STATEMENT := SUBSTR(P_STATEMENT, 1, LENGTH(P_STATEMENT) - 4);
            P_STATEMENT := P_STATEMENT || ' AND c.POSTED_DATE IS NOT NULL ORDER BY c.POSTED_DATE DESC';
            SYS.DBMS_OUTPUT.PUT_LINE(P_STATEMENT);


        END SEARCH_CONTENT;
END SEARCH_UTILITY;

CREATE PACKAGE DB_UTILITY IS

    FUNCTION normalize(input IN VARCHAR2)
        RETURN VARCHAR2;
    PROCEDURE dump_table_to_csv(p_tname IN VARCHAR2, p_filename IN VARCHAR2);

END DB_UTILITY;

CREATE PACKAGE BODY DB_UTILITY IS
    FUNCTION normalize(input IN VARCHAR2)
        RETURN VARCHAR2 AS
        normalizedText VARCHAR2(500);
        BEGIN
            SELECT utl_raw.cast_to_varchar2(nlssort(input, 'nls_sort=binary_ai'))
            INTO normalizedText
            FROM dual;
            RETURN normalizedText;
        END;


    PROCEDURE dump_table_to_csv(p_tname IN VARCHAR2, p_filename IN VARCHAR2)
    IS
        l_output      utl_file.file_type;
        l_theCursor   INTEGER DEFAULT dbms_sql.open_cursor;
        l_columnValue VARCHAR2(4000);
        l_status      INTEGER;
        l_query       VARCHAR2(1000)
        DEFAULT 'select * from ' || p_tname;
        l_colCnt      NUMBER := 0;
        l_separator   VARCHAR2(1);
        l_descTbl     dbms_sql.desc_tab;
        BEGIN
            l_output := utl_file.fopen('MY_DIR', p_filename, 'w');
            EXECUTE IMMEDIATE 'alter session set nls_date_format=''dd-mon-yyyy hh24:mi:ss''';

            dbms_sql.parse(l_theCursor, l_query, dbms_sql.native);
            dbms_sql.describe_columns(l_theCursor, l_colCnt, l_descTbl);

            FOR i IN 1 .. l_colCnt LOOP
                utl_file.put(l_output, l_separator || '"' || l_descTbl(i).col_name || '"'
                );
                dbms_sql.define_column(l_theCursor, i, l_columnValue, 4000);
                l_separator := ',';
            END LOOP;
            sys.utl_file.new_line(l_output);

            l_status := dbms_sql.execute(l_theCursor);

            WHILE (dbms_sql.fetch_rows(l_theCursor) > 0) LOOP
                l_separator := '';
                FOR i IN 1 .. l_colCnt LOOP
                    dbms_sql.column_value(l_theCursor, i, l_columnValue);
                    utl_file.put(l_output, l_separator || l_columnValue);
                    l_separator := ',';
                END LOOP;
                utl_file.new_line(l_output);
            END LOOP;
            dbms_sql.close_cursor(l_theCursor);
            utl_file.fclose(l_output);

            EXECUTE IMMEDIATE 'alter session set nls_date_format=''dd-MON-yy'' ';
            EXCEPTION
            WHEN OTHERS THEN
            EXECUTE IMMEDIATE 'alter session set nls_date_format=''dd-MON-yy'' ';
            RAISE;
        END dump_table_to_csv;

END DB_UTILITY;

CREATE INDEX login_index ON USERS (EMAIL, PASSWORD);
CREATE INDEX type_id_index ON CONTENTS (TYPE_ID);
CREATE INDEX firstname_index ON USERS (LOWER(FIRST_NAME));
CREATE INDEX lastname_index ON USERS (LOWER(LAST_NAME));
CREATE INDEX year_index ON CONTENTS (EXTRACT(YEAR FROM POSTED_DATE));
CREATE INDEX country_index ON LOCATIONS (LOWER(COUNTRY));
CREATE INDEX city_index ON LOCATIONS (LOWER(CITY));

CREATE OR REPLACE FUNCTION reencode(string IN VARCHAR2)
    RETURN VARCHAR2
AS
    encoded VARCHAR2(32767);
    TYPE array_t IS VARRAY (3) OF VARCHAR2(15);
    array   array_t := array_t('AL32UTF8', 'WE8MSWIN1252', 'WE8ISO8859P1');
    BEGIN
        FOR I IN 1..array.count LOOP
            encoded := CASE array(i)
                       WHEN 'AL32UTF8'
                           THEN string
                       ELSE CONVERT(string, 'AL32UTF8', array(i))
                       END;
            IF instr(
                   rawtohex(
                       utl_raw.cast_to_raw(
                           utl_i18n.raw_to_char(utl_raw.cast_to_raw(encoded), 'utf8')
                       )
                   ),
                   'EFBFBD'
               ) = 0
            THEN
                RETURN encoded;
            END IF;
        END LOOP;
        RAISE VALUE_ERROR;
    END;
