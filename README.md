## Name
spring boot test sample

## Overview
test code samples of spring boot: an framework of Java with junit and dbunit

## Requirement
eclipse: release 2020-03 is desirable.

## Description
See below: Japanese only.<br>
https://qiita.com/C_HERO/items/81527ca8117c6a530c96

## Usage
1. Download and import with eclipse as gradle project.

2. create table below onto your mysql database: version 5.7 is desirable.
```sql:
CREATE TABLE `items` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(20) COLLATE utf8mb4_bin DEFAULT NULL,
  `item_explanation` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  `category_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
```

3. fix application.yml following your environment.
