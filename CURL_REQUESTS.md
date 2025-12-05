# 抽奖系统 API 测试 CURL 请求

## 1. 健康检查接口

```bash
# 检查服务是否正常运行
curl -X GET http://localhost:8081/health
```

## 2. 参与者管理接口

```bash
# 添加参与者
curl -X POST "http://localhost:8081/api/draw/add-participant?name=张三"

curl -X POST "http://localhost:8081/api/draw/add-participant?name=李四"

curl -X POST "http://localhost:8081/api/draw/add-participant?name=王五"

# 查看所有参与者
curl -X GET http://localhost:8081/api/draw/participants

# 删除参与者
curl -X POST "http://localhost:8081/api/draw/remove-participant?name=张三"

# 清空所有参与者
curl -X POST http://localhost:8081/api/admin/clear-participants
```

## 3. 抽奖接口

```bash
# 开始抽奖 (默认抽取1个一等奖)
curl -X POST http://localhost:8081/api/draw/start

# 抽取指定数量的获奖者
curl -X POST "http://localhost:8081/api/draw/start?winnerCount=3&prizeName=iPhone16"

# 抽取特定奖项
curl -X POST "http://localhost:8081/api/draw/start?winnerCount=1&prizeName=特斯拉Model Y"
```

## 4. 手动发布公告接口

```bash
# 发布中奖公告
curl -X POST "http://localhost:8081/api/draw/announce?userName=赵六&prizeName=MacBook Pro"

# 发布特殊公告
curl -X POST "http://localhost:8081/api/draw/announce?userName=孙七&prizeName=豪华游轮一日游"
```

## 5. 管理员接口

```bash
# 发送普通广播消息
curl -X POST "http://localhost:8081/api/admin/broadcast?message=抽奖活动即将开始，请大家做好准备！"

# 发送系统通知
curl -X POST "http://localhost:8081/api/admin/notice?notice=由于系统维护，抽奖活动将延迟10分钟进行"

# 获取系统统计信息
curl -X GET http://localhost:8081/api/admin/stats
```

## 6. WebSocket 连接测试

要测试 WebSocket 连接，您可以使用浏览器打开 `http://localhost:8081/`，或者使用专门的 WebSocket 测试工具连接到：

```
ws://localhost:8081/ws-draw
```

订阅主题：`/topic/announcements`

## 7. 使用示例流程

```bash
# 1. 添加一批参与者
curl -X POST "http://localhost:8081/api/draw/add-participant?name=张三"
curl -X POST "http://localhost:8081/api/draw/add-participant?name=李四"
curl -X POST "http://localhost:8081/api/draw/add-participant?name=王五"
curl -X POST "http://localhost:8081/api/draw/add-participant?name=赵六"
curl -X POST "http://localhost:8081/api/draw/add-participant?name=钱七"

# 2. 查看参与者列表
curl -X GET http://localhost:8081/api/draw/participants

# 3. 发送活动开始通知
curl -X POST "http://localhost:8081/api/admin/notice?notice=抽奖活动正式开始！"

# 4. 进行抽奖
curl -X POST "http://localhost:8081/api/draw/start?winnerCount=1&prizeName=特等奖iPhone16"

# 5. 继续抽取其他奖项
curl -X POST "http://localhost:8081/api/draw/start?winnerCount=3&prizeName=二等奖小米电视"

# 6. 查看最终统计数据
curl -X GET http://localhost:8081/api/admin/stats
```