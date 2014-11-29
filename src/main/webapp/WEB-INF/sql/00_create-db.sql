DROP USER IF EXISTS oci_user;
DROP DATABASE IF EXISTS oci_db;
CREATE USER oci_user PASSWORD 'welcome';
CREATE DATABASE oci_db owner oci_user ENCODING = 'UTF-8';