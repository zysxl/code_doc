## DSL 语句

### 记录查询

#### filter 和query的区别

```json
filter	过滤器方式查询，它的查询不会计算相关性的分值，也不会对结果进行排序，因此效率高于query，查询的结果也可以被缓存。
quyery  方式查询，它的查询会计算相关性的分之，对结果值排序，评分等，也不会对查询的结果缓存。
```

#### 	有条件查询

##### 		叶子查询（单条件查询）

###### 			模糊匹配

​			

###### 			精确匹配

##### 		组合查询（多条件）

###### 			bool

###### 			constant_score

###### 			dis_max

##### 		连接查询（连表查询）

###### 			父子文档查询

###### 			嵌套文档查询

### 结果聚合



### 文档映射

#### 	ES 动态映射：插入数据的时候es会自动映射mapping ，对应的filed属性；

#### 静态映射：需要自己手动设置mapping的filed属性类型；

#### keyword和text类型的区别

text：该类型用于长文本的检索，创建索引前会将这些文本进行分词，转化为分词组合，建立索引。允许es来检索这些分词，text类型的不能用于排序和聚合操作。

keyword：该类型不能分词，可以被用来检索过滤、排序、聚合;keyword 类型不可用text进行分词模糊检索。

#### 修改原有的索引映射

1：如果要推倒现有的索引，需要重新建一个新的索引。

2：然后把之前索引里面的数据导入到新的索引里。

3：删除原来的索引。

4：为新的索引名称建立 别名为原来旧的索引名。

#### ES 乐观并发控制



#### 通过term和match查询数据时节点对应的数据类型 text 与keyworld的区别

##### term

1：term 查询 ； 当ES节点类型为keyword时，term不会分词，keyworld也不会分词，需要完全匹配才可以查询到结果。

2：term查询；当ES节点类型为text时，term不会分词，text 会按照mapping映射文档时候设置的分词器分词，所以term查询的条件必须是text字段分词后的某一个；才能查询到结果。

##### match

1: match 查询keyworld 字段；match会被分词；而keyword不会分词，match的需要跟keyword完全匹配才能查询到结果。

2: match 查询text的字段；matc会被分词；text 也会分词，只要match的分词结果和 text的分词结果有相同的就可以查询到结果。

#### ES 文档写入原理

JavaAPI客户端 ===》 选择一个DataNode 节点发送请求---》通过DataNode节点通过hash算法选择一个节点分片，然后主节点分片去同步数据到他对应的副本分片中，等同步完成后就返回到client端。

#### ES 检索原理

JavaApi客户端发起检索请求 ---》 选择一个DataNode节点发送广播请求 ---》每个分片返回对应的文档id和节点、分片信息；把数据进行汇总，全局排序==》将获取到需要得结果返回给客户端。

#### ES准实时索引实现原理

当数据写入到es分片中，会首先写入到内存中 ，然后在每隔1秒中通过内存的buffer生成一个sement标志位，并刷新的文件缓存中去，这时候数据就可以被检索到，然后在每隔30分钟刷新到磁盘中。

#### ES 在写数据时候，从文件缓存到写入硬盘中出现异常解决办法

在数据写入到内存中的时候，同时也会记录transLog日志，在refresh期间出现异常，会根据tansLog来进行数据恢复。等到文件缓存中的数据都刷入磁盘中，清空transLog。



# 通过name查询用户信息精确匹配  term
GET /es_db/_search
{
"query": {
"term": {
"name": "york"
}
}
}

#通过address分页查询用户信息，模糊匹配， match
GET /es_db/_search
{
"from": 0,
"size": 2,
"query": {
"match": {
"address": "xian"
}
}
}

#多字段的模糊查询与精确匹配 multi_match
GET /es_db/_search
{
"from": 0,
"size": 20,
"query": {
"multi_match": {
"query": "york",
"fields": ["name","remark"]
}
}
}


#未指定字段查询条件的query_string 含 AND 与 OR 条件
GET /es_db/_search
{
"from": 0,
"size": 20,
"query": {
"query_string": {
"query": "java OR 广"
}
}
}


#指定字段查询条件的query_string  含AND 与 OR 条件
GET /es_db/_search
{
"query": {
"query_string": {
"fields": ["address","remark"],
"query": "java OR 广厦"
}
}
}


#范围查询 range：范围关键字; gte:大于等于; lte:小于等于;gt:大于;lt:小于；now:当前时间
#查询年龄在19-28 之间的用户信息
GET /es_db/_search
{
"query": {
"range": {
"age": {
"gte": 19,
"lte": 28
}
}
}
}

#分页，输出字段，字段排序
GET /es_db/_search
{
"from": 0,
"size": 2,
"query": {
"range": {
"age": {
"gte": 10,
"lte": 28
}
}
},
"_source": ["address","age"],
"sort": {
"age": "desc"
}
}


#filter 过滤器查询
GET /es_db/_search
{
"query": {
"bool": {
"filter": {
"term": {
"age": "28"
}
}
}
}
}

#获取映射

GET /es_db/_mapping


#创建索引 以及 静态映射
DELETE /es_test

PUT /es_test
{
"mappings": {
"properties": {
"name":{
"type": "keyword",
"index": true,
"store": true
},
"sex":{
"type": "integer",
"index": true,
"store": true
},
"age":{
"type": "integer",
"index": true,
"store": true
},
"book":{
"type": "text",
"index": true,
"store": true
},
"address":{
"type": "text",
"index": true,
"store": true
}
}
}
}

#插入数据
PUT /es_test/_doc/1
{
"name":"张三",
"sex":1,
"age":25,
"book":"JAVA设计模式",
"address":"西安钟楼大厦"
}



GET /es_test/_search
{
"query": {
"term": {
"book": "JAVA设计模式"
}
}
}



GET /es_test/_search
{
"query": {
"match": {
"address": "西安钟楼大厦"
}
}
}


GET /es_test/_search
{
"query": {
"match_phrase": {
"address": "西安大厦"
}
}
}


PUT /es_test1
{
"mappings": {
"properties": {
"name":{
"type": "keyword",
"index": true,
"store": true
},
"sex":{
"type": "integer",
"index": true,
"store": true
},
"age":{
"type": "integer",
"index": true,
"store": true
},
"book":{
"type": "keyword",
"index": true,
"store": true
},
"address":{
"type": "keyword",
"index": true,
"store": true
}
}
}
}

PUT /es_test1/_doc/1
{
"name":"张三",
"sex":1,
"age":25,
"book":"JAVA设计模式",
"address":"西安钟楼大厦"
}

GET /es_test1/_search
{
"query": {
"term": {
"address": "西安钟楼大厦"
}
}
}

GET /es_test1/_search
{
"query": {
"match": {
"address": "西安钟楼大厦"
}
}
}

#ES 乐观锁控制并发

PUT /es_test2/_doc/1
{
"id":"1",
"name":"zhangsa",
"desc":"ES测试",
"createTime":"2021-07-17"
}
PUT /es_test2/_doc/2
{
"id":"2",
"name":"lisi",
"desc":"历史文化",
"createTime":"2021-07-17"
}

POST /es_test2/_update/1
{
"doc": {
"desc":"ES测试111"
}
}

#es 7.x 版本控制 _seq_no
GET /es_test2/_doc/1
POST /es_test2/_update/1/?if_seq_no=2&if_primary_term=1
{
"doc": {
"desc":"ES测试222"
}
}

# version_conflict_engine_exception 提示版本号异常不能修改数据
POST /es_test2/_update/1/?if_seq_no=2&if_primary_term=1
{
"doc": {
"desc":"ES测试222"
}
}

#ES 集群版本查看
GET  _cat/nodes?v

# 
GET /es_test/_mapping

GET /es_test/_search

POST _analyze
{
"analyzer": "standard",
"text":"JAVA设计模式"
}

GET /es_test1/_search
{
"query": {
"term": {
"book": {
"value": "JAVA设计模式"
}
}
}
}

GET /es_test/_search
{
"query": {
"term": {
"book": {
"value": "式"
}
}
}
}





GET /es_test1/_mapping

GET /es_test1/_search

POST _analyze
{
"analyzer": "standard",
"text":"JAVA设计模式"
}



GET /es_test/_search
{
"query": {
"match": {
"book": "式"
}
}
}


GET /es_test1/_search
{
"query": {
"match": {
"book": "JAVA设计模式"
}
}
}




























