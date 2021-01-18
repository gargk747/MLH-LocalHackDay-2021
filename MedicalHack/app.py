import web
import requests
import json
import Main

urls = (
  '/getTranslation', 'Index'
)

app = web.application(urls, globals())

render = web.template.render('templates/')

class Index(object):
    def GET(self):
        return render.hello_form()

    def POST(self):
        form = web.input(name="Nobody", greet="Hello")
        greeting =  (Main.getTransalation(str(form.greet)))
        text='<html><body bgcolor="lightblue"><h1 align="center">Simplify MyReport</h1> <form action="/getTranslation" method="POST">   <h3>Report Content:</h3><textarea type="text" rows="5" cols="150" name="greet">'+str(form.greet)+'</textarea><br/><input type="submit" value="Simplify"> </form><h3>Simplified Report</h3><table border=1><tr><th>Medical Terms</th><th>Simplified Report</th></tr>'
        for i in range(len(greeting.keys())):
            text+="<tr><td>"+greeting.keys()[i]+"</td><td>"+greeting.values()[i]+"</td></tr>"
        text+='</table></body>'
        f = open("templates/index.html","w")
        f.write(text)
        f.close()
        return render.index()

if __name__ == "__main__":
    app.run()