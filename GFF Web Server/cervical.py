#!C:\Python\python.exe

print ("Content-type:text/html\r\n\r\n")
print ("<html>")
print ("<head>")
print ("</head>")
print ("<body>")
print ("<h1> CERVICAL CANCER</h1>")
print ("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">")
print ("<link rel=\"stylesheet\" href=\"progress-circle.css\">")
print ("</body>")
print ("</html>")
import pandas as pd
import numpy as np
data=pd.read_csv(r'kag_risk_factors_cervical_cancer.csv')
data=data.replace({'?': np.nan}).fillna(0)
y=data.Biopsy
X= data.drop('Biopsy', axis=1)
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y,test_size=0.2)
from sklearn.linear_model import LogisticRegression
lr = LogisticRegression()
lr.fit(X_train,y_train)
k_pred = lr.predict(X_test)

#y_pred=lr.predict(pd.DataFrame(np.array(a)).T)
a = pd.read_csv("data.csv",header=None)
y_pred=lr.predict((pd.DataFrame(np.array(a))))
from sklearn.metrics import accuracy_score
z=accuracy_score(y_test, k_pred, normalize=True, sample_weight=None)
z=z*100;
z=int(z)
z=str(z)
print ("<div class=\"progress-circle progress-"+z+"\"><span>")
print (z)
print ("</span></div>")
print ("<br>")
#print (y_pred[0])
if y_pred[0]==0:
	print ("<p id=\"t2\" >You are safe</p>")
else:
    print ("<p id=\"t1\" >You are in danger</p>")
    print ("<html>")
    print ("<br>")
    print ("</html>")
    print ("<h4>Remedies for Cervical Cancer</h4>")
    
    print ("<p>Vitamin C  :studies have shown the protective, immunity-boosting benefits of good vitamin C intake on cervical health </p>")
    
    print ("<p>Zinc :zinc defiency is considered a risk factor for the development of cervical health issues</p>")
    
    print ("<p>Selenium  :in a recent study, supplementation with selenium improved the cervical health of over 75% of women, over a period of 6 months.</p>")
    
    print ("<p>In emergency contact:-Indian Institute of Pain Management \n10, Jawaharlal Nehru Road, 100 Feet Road, Landmark: Opposite Hotel Ambika Empier, ChennaiPhone: 044 6513 3333</p>")
