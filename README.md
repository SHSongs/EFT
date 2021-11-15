# ETF

[warning](https://stackoverflow.com/questions/46922415/does-yahoo-finance-ban-web-scrapy-or-notg)

## API docs
[API-docs](/API-docs)  

- [Chart docs](/API-docs/chart.md)  
원하는 티커의 가격 정보를 볼 수 있는 API 입니다


## 사용 방법
1. Repository를 clone 합니다.
```
git clone https://github.com/SHSongs/EFT.git
```

2. server 폴더에 접근합니다.
```
cd server
```

3. server를 실행합니다.
```
sbt run
```

4. 요청을 보냅니다.
```
http://localhost:9000/chart/aaa?period1=20211014&period2=20211016
```


## Prerequisites
This repository is tested ...
```
- MacOS
- Intelli J
- JDK 1.8.0

- Scala 2.13
- Play framework 2.8.8
```

## Docker
[shsongs/scala-play](https://hub.docker.com/repository/docker/shsongs/scala-play)

1. image를 docker hub에서 받아옵니다
```
docker pull shsongs/scala-play:0.1
```

2. image를 container로 만들어 실행시킵니다.
```
docker run -p 9000:9000 shsongs/scala-play:0.1
```

3. 요청을 보냅니다.
```
http://localhost:9000/chart/aaa?period1=20211014&period2=20211016
```
