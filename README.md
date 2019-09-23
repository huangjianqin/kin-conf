# kin-conf
  简易的分布式配置中心  
  参考至[xxl-conf](https://github.com/xuxueli/xxl-conf)

## kin-conf-diamond
   1. web接口
   2. mysql作配置持久化
   3. 读取顺序: 缓存(读超时缓存)->磁盘镜像->DB
   4. 镜像=所有配置的集合, 而不是一个key一个镜像文件, 减少大量小文件. 通过写文件缓存来完成镜像刷新.
    
## kin-conf-client
   1. 读配置顺序
        1. 进程内缓存
        2. 磁盘文件(主要应用于配置中心无法访问, app重启时仍然可以使用最近最新配置)
        3. 配置中心, http访问, 支持合并请求访问, 减少网络压力
   2. 通过long poll监控配置变化
    