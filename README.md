# 浦尔家后台项目（初始版）

#### 项目介绍
原始项目，使用graphql方式开发的接口。支持1.4版本的app，小程序，h5 等。

#### 软件架构
aobei-common     短信和推送定义的公共bean。</br>
aobei-common-spring-boot-start   短信，推送，MQ 等第三方服务的配置信息。使用spring方式注入。</br>
authserver       API接口的统一授权服务。获取访问接口必须的token信息。</br>
mbg              mybaties 的mgb 插件，生成dao层和service层的代码。自动实现一些常用的单表操作方法。</br>
sms-server       目前用来实现第三方服务的调用。采用redis队列读取的方式。来发送短信。推送等。</br>
train-console    浦尔家 后台管理系统。</br>
train-daoservice 浦尔家 dao和service 数据库操作。使用mbg工具生成的。</br>
trainapi         浦尔家 API接口服务，供小程序，app，h5 等客户端调用。</br>
parent           定义一些基本的依赖包的版本。</br>
cas-overlay-template  后台管理系统的单点登录系统。</br>      
#### 安装教程

1. 首次 clone 代码  首先install parent->install mbg->install aobei-common -> </br>
                      install aobei-commmon-spring-boot-start ->install train-daoservice -></br>
                      install train-console->install trainapi</br>
2. sms-server，authserver ，cas-overlay-template 单独部署</br>


开发人员名单：</br>

卜利文，李毅，刘永强，李歧珍，李硕，任丕明，许康
