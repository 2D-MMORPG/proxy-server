# 2D MMORPG Proxy Server

[![Build Status](https://travis-ci.org/2D-MMORPG/proxy-server.svg?branch=master)](https://travis-ci.org/2D-MMORPG/proxy-server)
[![Waffle.io - Columns and their card count](https://badge.waffle.io/2D-MMORPG/planning.svg?columns=all)](https://waffle.io/2D-MMORPG/planning) 
[![Lines of Code](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-proxy&metric=lines)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-proxy) 
[![Lines of Code](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-proxy&metric=ncloc)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-proxy) 
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.jukusoft.mmo%3Ammorpg-proxy)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-proxy) 
[![Coverage](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-proxy&metric=coverage)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-proxy) 
[![Technical Debt Rating](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-proxy&metric=sqale_debt_ratio)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-proxy) 
[![JUnit Tests Rating](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-proxy&metric=test_success_density)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-proxy) 
[![Security Rating](https://sonarcloud.io/api/badges/measure?key=com.jukusoft.mmo%3Ammorpg-proxy&metric=new_security_rating)](https://sonarcloud.io/dashboard/index/com.jukusoft.mmo%3Ammorpg-proxy) 
\
Proxy Server for 2D MMORPG.

## Create keystore

```bash
keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass password -validity 360 -keysize 2048
```

## Show SSL Debug

VM Options:
```bash
-Djavax.net.debug=ssl
```