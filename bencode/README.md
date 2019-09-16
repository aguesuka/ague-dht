Bencode 
=
bencode编码是bt协议中最常用的数据格式  
http://www.bittorrent.org/beps/bep_0003.html#bencoding


## 四种数据类型的对应实现如下  
- Dict 对应 ```BencodeMap```
- List 对应 ```BencodeList```
- String 对应 ```BencodeByteArray```  
- Integer 对应 ```BencodeInteger```  

    
## 使用
```$xslt
// byte[] 转BenocdeMap对象 
Bencode.parse(byte[])
```
```
// 对象转byte[]
// IBencode bencodeObject = new BencodeMap();
bencodeObject.toBencodeBytes();
```
```
// 从ByteBuff中读BenocdeMap对象
Bencode.parse(ByteBuff)
```
```
// 对象写入到ByteBuff中
// IBencode bencodeObject = new BencodeMap();
bencodeObject.writeToBuffer(ByteBuff)
```
## 其他
- ```BencodeParser``` 使用非递归实现了byte[] 转对象.  
- ```BencodeEncoder``` 使用递归实现了对象转byte[].  

如果需要最简单的实现,可以只拷贝这两个类到你的项目
