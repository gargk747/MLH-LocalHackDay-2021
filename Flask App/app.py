from flask import Flask, render_template, flash, request, session
from flask_session import Session
import os
from wtforms import Form, TextField, TextAreaField, validators, StringField, SubmitField


app = Flask(__name__)
app.config['DEBUG']=False
app.config['FLASK_ENV'] = 'development'
app.config.update(SECRET_KEY=os.urandom(24))
Session(app)

class ReviewForm(Form):
    name=TextField('Name: ', validators=[validators.DataRequired()])
    pname=TextField('Product Name: ', validators=[validators.DataRequired()])
    review=TextField('Review: ', validators=[validators.DataRequired()])

@app.route('/', methods=['GET', 'POST'])
def index():
    form = ReviewForm(request.form)

    if request.method=='POST':
        name=request.form['name']
        pname=request.form['pname']
        review=request.form['review']
        print( name, " " , pname, " " , review)

    if form.validate():
        flash('Review: '+ review)
        
    else:
        flash('Error: All the form fields are required.')

    return render_template('hello.html', form=form)

if __name__ == '__main__':
    app.run(debug=False)