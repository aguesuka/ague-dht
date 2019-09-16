DHT嗅探器  
  
伪装成BT下载客服端,加入DHT网络嗅探磁力链接.
需要JDK11版本,以及MAVEN3,以及公网ip

使用方法

clone仓库
```$xslt
git clone https://github.com/aguesuka/ague-dht
```
使用maven打包
```$xslt
cd  ague-dht
mvn package
```
运行程序
```$xslt
java -jar ./server/target/server-1.1.jar
```

默认的数据库是sqlite,路径为 ./server/sql/database;需要在 ./server/src/main/resources/application.yml 中设置spring.datasource.url的属性,推荐使用绝对路径.
其他配置参考类:cc.aguesuka.btfind.util.DhtServerConfig;
