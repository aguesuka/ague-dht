ague-dht
=
ague-dht 是一个磁力链接嗅探器,它伪装成BT下载客服端,加入DHT网络,嗅探磁力链接.  
每秒发送1000条请求时,平均3秒收到1次带有```infohash```的```announce_peer```请求;10次```get_peer```请求.
## 环境要求
需要JDK11,MAVEN3,以及公网ip

## 快速开始

- clone仓库
```$xslt
git clone https://github.com/aguesuka/ague-dht
```
- 使用maven打包
```$xslt
cd  ague-dht
mvn package
```
- 运行程序
```$xslt
java -jar ./server/target/server-1.1.jar
```
## 配置
注意:要保存数据先配置数据库  
默认的数据库是sqlite,路径为```./server/sql/database``` ;需要在``` ./server/src/main/resources/application.yml``` 中设置```spring.datasource.url```的属性,推荐使用绝对路径.
其他配置参考类```cc.aguesuka.btfind.util.DhtServerConfig```;
  
## 磁力链接转种子  
https://github.com/aguesuka/torrent-finder
