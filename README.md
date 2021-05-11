ague-dht
=
ague-dht 是一个磁力链接嗅探器, 它伪装成 BT 下载客服端, 加入 DHT 网络, 嗅探磁力链接.  
每秒发送1000条请求时, 平均 3 秒收到 1 次带有 infohash 的 announce_peer 请求; 10 次 get_peer请求.
## 环境要求
JDK11, MAVEN3, 以及公网 IP.

## 快速开始

- clone仓库
```
git clone https://github.com/aguesuka/ague-dht
```
- 使用maven打包
```
cd  ague-dht
mvn package
```
- 运行程序
```
java -jar ./server/target/server-1.2.jar
```
## 磁力链接转种子  
https://github.com/aguesuka/torrent-finder
