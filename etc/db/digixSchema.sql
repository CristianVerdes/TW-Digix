CREATE TABLESPACE tbs_perm_01
DATAFILE 'tbs_perm_01.dat'
SIZE 20M
ONLINE;

CREATE TEMPORARY TABLESPACE tbs_temp_01
TEMPFILE 'tbs_temp_01.dbf'
SIZE 5M
AUTOEXTEND ON;


CREATE USER digix
IDENTIFIED BY digixpwd
DEFAULT TABLESPACE tbs_perm_01
TEMPORARY TABLESPACE tbs_temp_01
QUOTA 50M ON tbs_perm_01;

GRANT CREATE SESSION TO digix;
GRANT CREATE TABLE TO digix;
GRANT CREATE VIEW TO digix;
GRANT CREATE ANY TRIGGER TO digix;
GRANT CREATE ANY PROCEDURE TO digix;
GRANT CREATE SEQUENCE TO digix;
GRANT CREATE SYNONYM TO digix;
GRANT EXECUTE ON DBMS_CRYPTO TO digix;
grant execute on utl_file to digix;
GRANT CREATE TYPE TO digix;