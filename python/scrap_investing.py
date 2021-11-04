import requests, re, time
from bs4 import BeautifulSoup

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) '
                  'Chrome/86.0.4240.198 Safari/537.36',
    'X-Requested-With': 'XMLHttpRequest'
}

historicalURL = "https://kr.investing.com/etfs/powershares-qqqq" + "-historical-data"

response = requests.get(historicalURL, headers=headers)
soup = BeautifulSoup(response.content, 'html.parser')

curr_id, smlId = None, None

for script in soup.findAll('script'):
    if script.string and "window.histDataExcessInfo" in script.string:
        histData = script.string.strip().replace("\n", "").replace(" ", "")
        curr_id, smlId = re.findall("\d+", histData)[0:2]
        print(curr_id, smlId)

title = soup.select('.float_lang_base_1.inlineblock')[0]
title = title.text

formData = {
    "curr_id": curr_id,
    "smlID": smlId,
    "header": title,
    "st_date": "2021/02/01",
    "end_date": "2021/02/10",
    "interval_sec": "Daily",
    "sort_col": "date",
    "sort_ord": "DESC",
    "action": "historical_data"
}

POSTURL = r"https://kr.investing.com/instruments/HistoricalDataAjax"

response = requests.post(POSTURL, headers=headers, data=formData)
f = open("QQQ", "w")
f.write(response.text)
f.close()

time.sleep(0.2)
