# L-novel
L-novel is one novel crawler

项目创作初衷是看到一个github项目[novel-plus](https://github.com/201206030/novel-plus),让自己也想做个小说爬虫。
参考代码，自己一步一步都去做大做强。



## 时间线
20220526 - 项目初始化，完成基础的爬虫

20220527 - 可以完整爬全站取单本小说信息，可以指定抓取指定数据源某本小说，下载指定小说

20220528 - 增加一个爬虫源，可以爬取动态网页

20220529 - 更新指定数据源的某本小说抓取逻辑，优化数据源2


## api
抓取指定数据源的分类信息

```http
curl --location --request POST '127.0.0.1:8080/lnovel/crawl/category' \
--form 'sourceId="1"' 
```



抓取指定数据源的全部小说

```http
curl --location --request POST '127.0.0.1:8080/lnovel/crawl' \
--form 'sourceId="1"' 
```



抓取指定数据源的指定小说

```http
curl --location --request POST '127.0.0.1:8080/lnovel/crawl/byBookId' \
--form 'sourceId="1"' \
--form 'bookId="72336"'
```


下载指定小说

```http
curl --location --request GET 'http://127.0.0.1:8080/lnovel/crawl/download?bookId=1'
```
