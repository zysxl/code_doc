### ES 环境安装配置

##### jar包下载

地址:https://elasticsearch.cn/download/

开工具环境版本

​	elasticsearch-7.6.1版本；jdk-8u11 版本；kibana-7.6.1版本

##### 环境安装

###### JDK：

```
1:tar -zxvf  jdk-8u11-linux-x64.tar.gz  #tar文件解压
2:JDK路径：/usr/software/jdk1.8.0_11
3:配置jdk环境
vim /etc/profile
JAVA_HOME=/usr/java 
JRE_HOME=/usr/java/jre CLASS_PATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib 			   			PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH 
export JAVA_HOME JRE_HOME CLASS_PATH PATH
4:source /etc/profile 修改生效
5:java -version 检查JDK版本
```

######  ES环境

```
1.tar -zxvf  elasticsearch-7.6.1-linux-x86_64.tar.gz #解压tar文件
2.vim /usr/software/elasticsearch-7.6.1/config/elasticsearch.yml
	单机安装请取消注释：node.name: node-1，否则无法正常启动。
	修改网络和端口，取消注释master节点，单机只保留一个node
	
	#ES 配置列表
	node.name: node-1
	network.host: 内网ip
	http.port: 9200 
	cluster.initial_master_nodes: ["node-1"]
3.修改vim /usr/software/elasticsearch-7.6.1/config/jvm.options内存设置
    -Xms1g 
    -Xmx1g
4.添加es用户，es默认root用户无法启动，需要改为其他用户
	添加用户 useradd es_dev 
	修改密码 passwd es_dev
5.改变es目录拥有者账号
	chown -R es_dev /usr/software/elasticsearch-7.6.1/
6.修改/etc/sysctl.conf
	vim /etc/sysctl.conf 
	末尾添加：vm.max_map_count=655360  执行sysctl -p 让其生效 sysctl -p
7.修改/etc/security/limits.conf
	vim /etc/security/limits.conf
	末尾添加：
	* soft nofile 65536 
	* hard nofile 65536 
	* soft nproc 4096 
	* hard nproc 4096
8.配置防火墙
  port：9200
9.启动ES
  su es_dev
  /usr/software/elasticsearch/bin/elasticsearch
10.配置完成：浏览器访问测试。 ip:9200
   查看es状态
   curl http://172.24.110.29:9200
   curl http://172.24.110.29:9200/_cat/health?v
```

#####  ElasticSearch的图形化插件

###### **安装配置****kibana

1.root账户下操作；

​	tar -zxvf  kibana-7.6.1-linux-x86_64.tar.gz

2.改变es目录拥有者账号

​	chown -R es_dev /usr/software/kibana-7.6.1

3.设置访问权限

​	chmod -R 777 /usr/software/kibana-7.6.1

4.修改配置文件

​	vim /usr/software/kibana/config/kibana.yml

```
修改端口，访问ip,elasticsearch服务器ip
server.port: 5601 
server.host: "0.0.0.0" 
elasticsearch.hosts: ["http://内外ip:9200"]
```

5.配置防火墙
  port：5601

6.配置完成启动：

切换用户 

su es_dev

./bin/kibana(路径：/usr/software/kibana)

7.访问ip:5601

##### **Elasticsearch集成*IK***分词器

1.在elasticsearch安装目录的plugins目录下新建 analysis-ik 目录

```
#新建analysis-ik文件夹 
mkdir analysis-ik 
#切换至 analysis-ik文件夹下 
cd analysis-ik 
#上传资料中的 
elasticsearch-analysis-ik-7.6.1.zip
#解压 unzip elasticsearch-analysis-ik-7.6.1.zip
#解压完成后删除zip rm -rf elasticsearch-analysis-ik-7.6.1.zip
```

2.重启Elasticsearch 和Kibana

IK分词器有两种分词模式：ik_max_word和ik_smart模式

1）ik_max_word (常用)

会将文本做最细粒度的拆分

2）ik_smart

会做最粗粒度的拆分

##### 扩展词典使用

自定义扩展词库

1）进入到 confifig/analysis-ik/(**插件命令安装方式**) 或 plugins/analysis-ik/confifig(**安装包安装方式**) 目录

下, 新增自定义词典

vim york_ext_dict.dic 

2）将我们自定义的扩展词典文件添加到IKAnalyzer.cfg.xml配置中

vim IKAnalyzer.cfg.xml

```xml
<?xml version="1.0" encoding="UTF-8"?> 
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd"> 
	<properties> 
        <comment>IK Analyzer 扩展配置 </comment>
        <!--用户可以在这里配置自己的扩展字典 --> 
        <entry key="ext_dict">york_ext_dict.dic</entry> 
        <!--用户可以在这里配置自己的扩展停止词字典-->
		<entry key="ext_stopwords">york_stop_dict.dic</entry> 
        <!--用户可以在这里配置远程扩展字典 --> 
        <!-- <entry key="remote_ext_dict">http://ip:8080/tag.dic</entry> -->
        <!--用户可以在这里配置远程扩展停止词字典--> 
        <!-- <entry key="remote_ext_stopwords">words_location</entry> -->
</properties>
```

3) 停用词典使用

4) 配置IK同义词

创建/confifig/analysis-ik/synonym.txt 文件，输入一些同义词并存为 utf-8 格式,

```
例如
test,测试 china,中国
```

重启Elasticsearch

1)）创建索引时，使用同义词配置，示例模板如下

```json
PUT /索引名称
{
    "settings":{
        "analysis":{
            "filter":{
                "word_sync":{
                    "type":"synonym",
                    "synonyms_path":"analysis-ik/synonym.txt"
                }
            },
            "analyzer":{
                "ik_sync_max_word":{
                    "filter":[
                        "word_sync"
                    ],
                    "type":"custom",
                    "tokenizer":"ik_max_word"
                },
                "ik_sync_smart":{
                    "filter":[
                        "word_sync"
                    ],
                    "type":"custom",
                    "tokenizer":"ik_smart"
                }
            }
        }
    },
    "mappings":{
        "properties":{
            "字段名":{
                "type":"字段类型",
                "analyzer":"ik_sync_smart",
                "search_analyzer":"ik_sync_smart"
            }
        }
    }
}
以上配置定义了ik_sync_max_word和ik_sync_smart这两个新的 analyzer，对应 IK 的 ik_max_word 和ik_smart 两种分词策略。ik_sync_max_word和 ik_sync_smart都会使用 synonym filter 实现同义词转换
```

##### 索引操作

​	打开索引

```
POST /索引名称/_open
```

​	关闭索引

```
POST /索引名称/_close
```

​	删除索引库

```
DELETE /索引名称1,索引名称2,索引名称3...
```

​	查看索引

```
GET /索引名称
```

##### 映射操作

```

```

