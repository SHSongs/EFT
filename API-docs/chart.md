**Chart**
----
  Returns json data about chart.

* **URL**

  /chart/{ticker}  
  
example) [http://localhost:9000/chart/aaa?period1=20211014&period2=20211016](http://localhost:9000/chart/aaa?period1=20211014&period2=20211016)  

* **Method:**

  `GET`
  
*  **URL Params**

   **Required:**
 
   `ticker=[string]`  주식의 ticker를 입력합니다 example) QQQ


* **query Params**

   **Required:**  

    `period1=[string]`  기간의 시작 example) 20211014

    `period2=[string]`  기간의 끝  example) 20211016

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** 

```json 
{
  "type": "chart",
  "data": {
    "name": "aaa",
    "start": "20211014",
    "end": "20211016",
    "history": [
      {
        "Date": "Oct 15, 2021",
        "Open": "25.01",
        "High": "25.01",
        "Low": "25.00",
        "Close": "25.00",
        "Adj Close": "24.98",
        "Volume": "600"
      },
      {
        "Date": "Oct 14, 2021",
        "Open": "25.00",
        "High": "25.00",
        "Low": "25.00",
        "Close": "25.00",
        "Adj Close": "24.98",
        "Volume": "100"
      }
    ]
  }
}
```

* **Sample Call:**  

[sample code](https://github.com/SHSongs/EFT/blob/main/client/main.py)

```py
import urllib.request as ul
import json
import pandas as pd


def get_chart(ticker, period1, period2):
    url = f"http://localhost:9000/chart/{ticker}?period1={period1}&period2={period2}"

    request = ul.Request(url)
    response = ul.urlopen(request)

    rescode = response.getcode()
    if rescode != 200:
        return None

    responsedata = response.read()
    my_json = responsedata.decode('utf8').replace("'", '"')
    data = json.loads(my_json)

    return data["data"]["history"]


info = get_chart("aaa", 20211015, 20211104)
df = pd.json_normalize(info)
df.to_csv("aaa_chart.csv")

print(df)
```
