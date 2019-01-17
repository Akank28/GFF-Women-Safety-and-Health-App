#!C:\Python\python.exe
print ("Content-type:text/html\r\n\r\n")
print ("<html>")
print ("<head>")
print ("</head>")
print ("<body>")
#print ("<h2> PREDICTION MODEL FOR CERVICAL CANCER</h2>")
#print ("</body>")
print ("</html>")
import urllib.request
from bs4 import BeautifulSoup
import simplejson as json
url = "http://www.newincept.com/central-government-schemes-for-women-empowerment.html"
#download the URL and extract the content to the variable html 
request = urllib.request.Request(url)
print (request)
html = urllib.request.urlopen(request).read()
soup = BeautifulSoup(html,'html.parser')
#get the HTML of the table called site Table where all the links are displayed
main_table = soup.find("div",attrs={'class':'col-sm-12'})
links = main_table.find_all("a",class_="borderlink")
extracted_records = []
for link in links: 
    title = link.text
    url = link['href']
    record = {'title':title,
              'url':url
        }
    extracted_records.append(record)
print (extracted_records)
print ("<html>")
print ("<br>")
print ("</html>")
"""with open('data.json', 'w') as outfile:
    json.dump(extracted_records, outfile)"""