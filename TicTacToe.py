from tkinter import messagebox
from tkinter import *


i = 1
def back():
    root1.destroy()
    game_entry()

def rule():
    root.destroy()
    global root1
    
    root1 = Tk()
    root1.title('TIC-TAC-TOE')
    root1.geometry('350x350')
    root1.configure(bg='#FF9999')
    
    lbl = Label(root1,text='TIC TAC TOE \n RULES',fg='#006FDD',font=('Ebrima',12),bg='#FF9999')
    lbl.place(x=130,y=20)
    
    lbl0 = Label(root1,text='You play on a 3x3 game board\nThe first player is known as "O" and the second is "X"',bg='#FF9999',font=('italic',10))
    lbl0.place(x=6,y=90)
    
    lbl1 =Label(root1,text='Players alternate placing Xs and Os on the game board\n until either oppent has 3 in a row or all 9 squares are filled.',bg='#FF9999',font=('italic',10))
    lbl1.place(x=3,y=150)
    
    btn = Button(root1,text="BACK",width="10",command=back,bg='#FFBB77')
    btn.place(x=140,y=220)
    root1.mainloop()
    
def click():
    global Player1
    global Player2
    
    if E1.get() == '' or E2.get() == '' :
        Player1 = 'Player1'
        Player2 = "Player2"   
    else:     
        Player1 = E1.get()
        Player2 = E2.get()
    game_start()

def exit():
    root.destroy()
    
def quit():
    window.destroy()
    game_entry()
    
def game_entry():
    global E1
    global E2
    global root
    
    root = Tk()

    root.title("Tic_tac_toe")

    root.geometry('350x350')
    root.configure(bg='#FF9999')

    l1 = Label(root,text="Enter Player1 name",bg='#FF9999')
    l1.place(x=20,y=50)

    l2 = Label(root,text="Enter Player2 name",bg='#FF9999')
    l2.place(x=20,y=110)

    E1 = Entry(root,width=30)
    E1.place(x=130,y=50)
    E1.focus()
   

    E2 = Entry(root,width=30)
    E2.place(x=130,y=110)

    b1 = Button(root,text="NEXT",width="10",command=click,bg='#FFBB77')
    b1.place(x=232,y=150)
    
    b2 = Button(root,text="EXIT",width="10",command=exit,bg='#FFBB77')
    b2.place(x=175,y=200)
    
    b3 = Button(root,text="RULES",width="10",command=rule,bg='#FFBB77')
    b3.place(x=130,y=150)
    
    root.mainloop()

def game_start():
    global b1,b2,b3,b4,b5,b6,b7,b8,b9
    global window
    root.destroy()
        
    window = Tk()

    window.title("tic tac toe")

    window.geometry('350x350')
    
    window.configure(bg='#FF9999')

    lbl = Label(window,text="Let's Play game",font=('Arial Bold',15),fg='Red',bg='#FF9999')
    lbl.place(x=60,y=10)

    b1 = Button(window,text="",width=10,height=5,command=clicked1,bg="#4BE93C")
    b1.place(x=50,y=50)

    b2 = Button(window,text="",width=10,height=5,command=clicked2,bg='#BBFFB5')
    b2.place(x=130,y=50)

    b3 = Button(window,text="",width=10,height=5,command=clicked3,bg='#4BE93C')
    b3.place(x=210,y=50)

    b4 = Button(window,text="",width=10,height=5,command=clicked4,bg='#BBFFB5')
    b4.place(x=50,y=130)

    b5 = Button(window,text="",width=10,height=5,command=clicked5,bg='#4BE93C')
    b5.place(x=130,y=130)

    b6 = Button(window,text="",width=10,height=5,command=clicked6,bg='#BBFFB5')
    b6.place(x=210,y=130)

    b7 = Button(window,text="",width=10,height=5,command=clicked7,bg='#4BE93C')
    b7.place(x=50,y=210)

    b8 = Button(window,text="",width=10,height=5,command=clicked8,bg='#BBFFB5')
    b8.place(x=130,y=210)

    b9 = Button(window,text="",width=10,height=5,command=clicked9,bg='#4BE93C')
    b9.place(x=210,y=210)
    
    b10 = Button(window,text="QUIT",width=10,command=quit,bg='#FFBB77')
    b10.place(x=130,y=320)
    
    window.mainloop()
    

def player2():
    if((b1['text']=='X') and (b2['text']=='X') and (b3['text']=='X')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player2}")
        window.destroy()
    elif((b4['text']=='X') and (b5['text']=='X') and (b6['text']=='X')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player2}")
        window.destroy()
    elif((b7['text']=='X') and (b8['text']=='X') and (b9['text']=='X')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player2}")
        window.destroy()
    elif((b1['text']=='X') and (b4['text']=='X') and (b7['text']=='X')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player2}")
        window.destroy()
    elif((b2['text']=='X') and (b5['text']=='X') and (b8['text']=='X')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player2}")
        window.destroy()
    elif((b3['text']=='X') and (b6['text']=='X') and (b9['text']=='X')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player2}")
        window.destroy()
    elif((b1['text']=='X') and (b5['text']=='X') and (b9['text']=='X')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player2}")
        window.destroy()
    elif((b3['text']=='X') and (b5['text']=='X') and (b7['text']=='X')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player2}")
        window.destroy()
    elif(i==9):
        messagebox.showinfo("Congratulation","Game is tied")
        window.destroy()

def player1():
    if((b1['text']=='O') and (b2['text']=='O') and (b3['text']=='O')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player1}")
        window.destroy()
    elif((b4['text']=='O') and (b5['text']=='O') and (b6['text']=='O')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player1}")
        window.destroy()
    elif((b7['text']=='O') and (b8['text']=='O') and (b9['text']=='O')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player1}")
        window.destroy()
    elif((b1['text']=='O') and (b4['text']=='O') and (b7['text']=='O')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player1}")
        window.destroy()
    elif((b2['text']=='O') and (b5['text']=='O') and (b8['text']=='O')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player1}")
        window.destroy()
    elif((b3['text']=='O') and (b6['text']=='O') and (b9['text']=='O')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player1}")
        window.destroy()
    elif((b1['text']=='O') and (b5['text']=='O') and (b9['text']=='O')):
        messagebox.showinfo("Cogratulation",f"winner\n\n{Player1}")
        window.destroy()
    elif((b3['text']=='O') and (b5['text']=='O') and (b7['text']=='O')):
        messagebox.showinfo("Cogratulation",f'winner\n\n{Player1}')
        window.destroy()
    elif(i==9):
        messagebox.showinfo("Congratulation","Game is tied")
        window.destroy()

def clicked1():
    global i
    if(i%2==0):
        b1["text"] = "X"
        b1.configure(state="disabled")
        player2() 

    else:
        b1["text"] = "O"
        b1.configure(state="disabled")
        player1()
    i+=1

def clicked2():
    global i
    if(i%2==0):
        b2["text"] = "X"
        b2.configure(state="disabled")
        player2()
    else:
        b2["text"] = "O"
        b2.configure(state="disabled")
        player1()
    i+=1

def clicked3():
    global i
    if(i%2==0):
        b3["text"] = "X"
        b3.configure(state="disabled")
        player2()
    else:
        b3["text"] = "O"
        b3.configure(state="disabled")
        player1()
    i+=1

def clicked4():
    global i
    if(i%2==0):
        b4["text"] = "X"
        b4.configure(state="disabled")
        player2()
    else:
        b4["text"] = "O"
        b4.configure(state="disabled")
        player1()
    i+=1

def clicked5():
    global i
    if(i%2==0):
        b5["text"] = "X"
        b5.configure(state="disabled")
        player2()
    else:
        b5["text"] = "O"
        b5.configure(state="disabled")
        player1()
    i+=1

def clicked6():
    global i
    if(i%2==0):
        b6["text"] = "X"
        b6.configure(state="disabled")
        player2()
    else:
        b6["text"] = "O"
        b6.configure(state="disabled")
        player1()
    i+=1

def clicked7():
    global i
    if(i%2==0):
        b7["text"] = "X"
        b7.configure(state="disabled")
        player2()
    else:
        b7["text"] = "O"
        b7.configure(state="disabled")
        player1()
    i+=1

def clicked8():
    global i
    if(i%2==0):
        b8["text"] = "X"
        b8.configure(state="disabled")
        player2()
    else:
        b8["text"] = "O"
        b8.configure(state="disabled")
        player1()
    i+=1

def clicked9():
    global i
    if(i%2==0):
        b9["text"] = "X"
        b9.configure(state="disabled")
        player2()
    else:
        b9["text"] = "O"
        b9.configure(state="disabled")
        player1()
    i+=1



game_entry()