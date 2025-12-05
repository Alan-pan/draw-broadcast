@echo off
echo 正在测试抽奖系统API...

echo.
echo 1. 检查服务健康状态
curl -X GET http://localhost:8082/health
echo.

echo.
echo 2. 添加测试参与者
curl -X POST "http://localhost:8082/api/draw/add-participant?name=张三"
echo.
curl -X POST "http://localhost:8082/api/draw/add-participant?name=李四"
echo.
curl -X POST "http://localhost:8082/api/draw/add-participant?name=王五"
echo.
curl -X POST "http://localhost:8082/api/draw/add-participant?name=赵六"
echo.
curl -X POST "http://localhost:8082/api/draw/add-participant?name=钱七"
echo.

echo.
echo 3. 查看所有参与者
curl -X GET http://localhost:8082/api/draw/participants
echo.

echo.
echo 4. 发送系统通知
curl -X POST "http://localhost:8082/api/admin/notice?notice=欢迎参加本次抽奖活动！"
echo.

echo.
echo 5. 开始抽奖
curl -X POST "http://localhost:8082/api/draw/start?winnerCount=1&prizeName=iPhone16 Pro"
echo.

echo.
echo 6. 抽取多个获奖者
curl -X POST "http://localhost:8082/api/draw/start?winnerCount=2&prizeName=AirPods Pro"
echo.

echo.
echo 7. 手动发布公告
curl -X POST "http://localhost:8082/api/draw/announce?userName=测试用户&prizeName=特别奖励"
echo.

echo.
echo 8. 查看系统统计
curl -X GET http://localhost:8082/api/admin/stats
echo.

echo.
echo API测试完成！
pause