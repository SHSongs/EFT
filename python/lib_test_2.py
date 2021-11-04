from yahoo_finance import Share

company = Share('005930.KS')
company.get_historical('2016-07-04', '2016-07-08')  # not work
