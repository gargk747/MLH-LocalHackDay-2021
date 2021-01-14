import sendgrid
import os
from sendgrid.helpers.mail import *

sg = sendgrid.SendGridAPIClient(api_key=os.environ.get('SENDGRID_API_KEY'))
from_email = Email("gargk747@gmail.com")
to_email = To("2018ucp1674@mnit.ac.in")
subject = "First trial of Sendgrid"
content = Content("text/plain", "First mail of sendgrid! ")
mail = Mail(from_email, to_email, subject, content)
response = sg.client.mail.send.post(request_body=mail.get())
print(response.status_code)
print(response.body)
print(response.headers)