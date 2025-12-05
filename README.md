# 实时抽奖广播系统

一个基于Spring Boot、WebSocket和RabbitMQ的实时抽奖结果广播系统，可以实时推送抽奖结果给所有在线用户，支持多节点部署。

## 功能特点

1. **实时推送**：使用WebSocket技术实现实时消息推送
2. **抽奖管理**：支持添加/删除参与者、开始抽奖等功能
3. **多奖项支持**：可以设置不同的奖项和中奖人数
4. **可视化界面**：提供Web界面展示抽奖结果
5. **RESTful API**：提供完整的API接口供外部调用
6. **系统监控**：提供系统状态查询和管理接口
7. **多节点支持**：通过RabbitMQ实现多节点部署，确保消息在整个集群中同步

## 技术栈

- Spring Boot 3.2
- Spring WebSocket
- Spring AMQP (RabbitMQ)
- Lombok
- Java 17

## 快速开始

### 1. 克隆项目

```bash
git clone <项目地址>
```

### 2. 安装并启动RabbitMQ

确保已安装并运行RabbitMQ服务器，默认端口为5672，管理界面端口为15672。

### 3. 构建项目

```bash
mvn clean package
```

### 4. 运行项目

```bash
mvn spring-boot:run
```

或者

```bash
java -jar target/realtime-draw-broadcast-0.0.1-SNAPSHOT.jar
```

### 5. 启动多个实例（多节点）

可以通过指定不同的端口来启动多个实例：

```bash
java -jar target/realtime-draw-broadcast-0.0.1-SNAPSHOT.jar --server.port=8082
java -jar target/realtime-draw-broadcast-0.0.1-SNAPSHOT.jar --server.port=8082
```

### 6. 访问应用

- Web界面: http://localhost:8082/
- 健康检查: http://localhost:8082/health
- WebSocket端点: /ws-draw

## API接口文档

### 健康检查接口

- `GET /health` - 检查服务状态

### 抽奖相关接口

- `POST /api/draw/add-participant?name={name}` - 添加参与者
- `POST /api/draw/remove-participant?name={name}` - 删除参与者
- `GET /api/draw/participants` - 获取所有参与者
- `POST /api/draw/start?winnerCount={count}&prizeName={prize}` - 开始抽奖
- `POST /api/draw/announce?userName={user}&prizeName={prize}` - 手动发布公告

### 管理接口

- `POST /api/admin/broadcast?message={message}` - 发送广播消息
- `POST /api/admin/notice?notice={notice}` - 发送系统通知
- `POST /api/admin/clear-participants` - 清空所有参与者
- `GET /api/admin/stats` - 获取系统统计信息

## WebSocket通信

客户端可以通过WebSocket连接到 `/ws-draw` 端点，并订阅 `/topic/announcements` 主题来接收实时的抽奖结果广播。

无论客户端连接到哪个节点，都能接收到整个集群中的所有抽奖消息。

## 前端界面

访问 `http://localhost:8082/` 可以看到前端界面，具有以下功能：

1. 参与者管理（添加、查看、清空）
2. 抽奖控制（设置中奖人数和奖品名称）
3. 手动发布公告
4. 实时显示中奖结果

## 使用curl测试

参考项目中的 `CURL_REQUESTS.md` 和 `test-api.bat` 文件进行API测试。

## 自定义配置

可以在 `application.yml` 中修改以下配置：

```yaml
server:
  port: 8082  # 服务端口

spring:
  rabbitmq:
    host: localhost     # RabbitMQ主机地址
    port: 5672          # RabbitMQ端口
    username: guest     # 用户名
    password: guest     # 密码

websocket:
  endpoint: /ws-draw      # WebSocket端点
  topic-prefix: /topic    # 主题前缀
```

## 多节点部署架构

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Node 1        │    │   Node 2        │    │   Node N        │
│                 │    │                 │    │                 │
│  ┌──────────┐   │    │  ┌──────────┐   │    │  ┌──────────┐   │
│  │WebSocket │◄──┼────┼──┤WebSocket │◄──┼────┼──┤WebSocket │◄──┼───► Clients
│  │Clients   │   │    │  │Clients   │   │    │  │Clients   │   │
│  └──────────┘   │    │  └──────────┘   │    │  └──────────┘   │
│        ▲        │    │        ▲        │    │        ▲        │
│        │        │    │        │        │    │        │        │
│  ┌──────────┐   │    │  ┌──────────┐   │    │  ┌──────────┐   │
│  │MQ        │   │    │  │MQ        │   │    │  │MQ        │   │
│  │Consumer  │   │    │  │Consumer  │   │    │  │Consumer  │   │
│  └──────────┘   │    │  └──────────┘   │    │  └──────────┘   │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                        ┌────────▼────────┐
                        │  RabbitMQ       │
                        │  Fanout Exchange│
                        └─────────────────┘
```

当任何一个节点产生抽奖消息时：
1. 消息被发送到RabbitMQ的Fanout交换机
2. Fanout交换机会将消息广播到所有绑定的队列
3. 每个节点上的MQ消费者接收到消息
4. 每个节点将消息通过WebSocket广播给连接到该节点的客户端

这样就实现了多节点间的消息同步，保证所有客户端都能收到完整的抽奖消息。

## 部署说明

1. 安装并启动RabbitMQ服务器
2. 构建项目：`mvn clean package`
3. 将生成的jar包部署到多台服务器
4. 分别在各台服务器上运行：
   ```bash
   java -jar realtime-draw-broadcast-0.0.1-SNAPSHOT.jar --server.port=[端口号]
   ```

## 开发说明

1. 项目采用Maven构建
2. 主要包结构：
   - `config`: 配置类
   - `controller`: 控制器
   - `entity`: 实体类
   - `service`: 业务逻辑层
3. 使用Lombok简化Java Bean代码
4. 使用RabbitMQ实现多节点消息同步