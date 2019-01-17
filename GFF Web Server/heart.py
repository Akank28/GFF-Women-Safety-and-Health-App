#!C:\Python\python.exe

print ("Content-type:text/html\r\n\r\n")
print ("<html>")
print ("<head>")
print ("<title>Heart Disease</title>")
print ("</head>")
print ("<h1> HEART DISEASE</h1>")
print ("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">")
print ("<link rel=\"stylesheet\" href=\"progress-circle.css\">")
print ("<body>")

import pandas as pd
import numpy as np
df=pd.read_table(r"heart.dat.txt",sep="\s+")
df.columns=["age",
 "sex",
"chest pain type (4 values)",
 "resting blood pressure",
"serum cholestoral in mg/dl", 
"fasting blood sugar > 120 mg/dl", 
"resting electrocardiographic results (values 0,1,2)",
"maximum heart rate achieved",
 "exercise induced angina",
 "oldpeak = ST depression induced by exercise relative to rest", 
 "the slope of the peak exercise ST segment",
 "number of major vessels (0-3) colored by flourosopy",
"thal: 3 = normal; 6 = fixed defect; 7 = reversable defect", "Prediction"
]
df=pd.DataFrame(df)
df=df[df.sex!=1]
y=df.Prediction
X= df.drop("Prediction", axis=1)
from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y,test_size=0.2)
from sklearn.linear_model import LogisticRegression
lr = LogisticRegression()
lr.fit(X_train,y_train)
k_pred = lr.predict(X_test)
a=[67,0,3,115,564,0,2,160,0,1.6,2,0,7]
y_pred=lr.predict(pd.DataFrame(np.array(a)).T)
from sklearn.metrics import accuracy_score
z=accuracy_score(y_test, k_pred, normalize=True, sample_weight=None)
z=z*100;
z=int(z)
z=str(z)
print ("<div class=\"progress-circle progress-"+z+"\"><span>")
print (z)
print ("</span></div>")
print ("<html>")
print ("<br>")
print ("</html>")
if y_pred[0]==0:
	print ("<p id=\"t2\" >You are safe</p>")
else:
    print ("<p id=\"t1\" >You are in danger</p><br>")
   
    print ("<br>")
    
    
   
print ("</body>")
print ("</html>")

