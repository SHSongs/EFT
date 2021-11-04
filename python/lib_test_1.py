import yfinance as yf

data = yf.download(['AAPL', 'QQQ'], start='2020-03-04', end='2021-03-01')
print(data)


