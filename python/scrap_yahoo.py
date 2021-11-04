import requests
from bs4 import BeautifulSoup

headers = {
      'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36'}

url = r"https://finance.yahoo.com/quote/QQQ/history?period1=1634256000&period2=1635984000&interval=1d&filter=history" \
      r"&frequency=1d&includeAdjustedClose=true "

response = requests.get(url, headers=headers)
soup = BeautifulSoup(response.content, 'html.parser')

print(soup)
Html_file= open("filename.html","w")
Html_file.write(response.text)
Html_file.close()
