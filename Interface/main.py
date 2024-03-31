import os
import subprocess as sb
from time import sleep
from tkinter import Tk, Button, Label, Canvas, PhotoImage, Frame, Text, Listbox, font

class Screen:
    def __init__(self) -> None:
        self.screen = Tk()

    def configureWindow(self) -> None:
        self.screen.title("Autômato+")
        self.screen.iconbitmap(dir + "/Interface/img/icone.ico")
        self.screen.resizable(False, False)
        self.screen.configure(bg="#BEB6E0")
        x = self.screen.winfo_screenwidth() // 2 - 400
        y = self.screen.winfo_screenheight() // 11
        self.screen.geometry(f"800x600+{x}+{y}")

    def closeWindow(self) -> None:
        self.screen.destroy()

    def cleanWindow(self, frame, filtro=None) -> None:
        for componente in frame.winfo_children():
            if componente in filtro:
                componente.place_forget()
        comand = True

def novoProcessoJava(caminhoJar):
    try:
        sb.Popen(['java', '-jar', caminhoJar], creationflags=sb.CREATE_NO_WINDOW)
    except sb.CalledProcessError:
        print("Verifique a configuração do Java.")

def dica(botao, imgs):
    image = botao.cget("image")
    if image == str(imgs[0]):
        botao.config(image=imgs[1])
        botao.config(command=lambda: dica(botao, imgs))
        janela_dica = Tk()
        janela_dica.title("Dicas")
        janela_dica.iconbitmap(dir + "/Interface/img/icone.ico")
        janela_dica.resizable(False, False)
        janela_dica.configure(bg="#1C1C1C")
        x = janela_dica.winfo_screenwidth() // 2 - 100
        y = janela_dica.winfo_screenheight() // 4
        janela_dica.geometry(f"300x300+{x}+{y}")
        label = Label(
                    janela_dica, text="\nDicas:",
                    font=('Arial', 12, 'bold'),
                    fg='white',
                    bg='#1C1C1C',
                )
        label.pack()

        janela_dica.mainloop()
    
    elif image == str(imgs[1] ):
        botao.config(image=imgs[0])
        botao.config(command=lambda: dica(botao, imgs))

def addVingadorMaisForte():
    labelAlcides.place(x=-15, y=332)

def addButtonJflap():
    bt_sim = Button(
                master=frame_manipulacao,
                background='#D9D9D9', 
                font=('Arial', 10, 'bold'),
                text="Sim",
                borderwidth=4,
                width=4,
                height=2,
                command=lambda caminho=dir + "/ProjetoMinimizacao/tests/JFLAP.jar": (janela.cleanWindow(frame_manipulacao, [bt_sim, bt_nao, label_nome, labelAlcides, label_jflap]), novoProcessoJava(caminho)), 
            )
    bt_nao = Button(
                master=frame_manipulacao,
                background='#D9D9D9', 
                font=('Arial', 10, 'bold'),
                text="Não",
                borderwidth=4,
                width=4,
                height=2,
                command=lambda : janela.cleanWindow(frame_manipulacao, [bt_sim, bt_nao, label_nome, labelAlcides, label_jflap]),
            )
    bt_sim.place(x=240,y=400)
    bt_nao.place(x=315,y=400)

def isText():
    return campoTxt.get("1.0", "end-1c")

def listBox(event=None):
    print(listbox.get(listbox.curselection()))

def conversaStark(event=None):
    if(comand and (isText().lower() in lista_comandos)):
        addVingadorMaisForte()
        # listbox.place(x=220, y=150)
        addButtonJflap()
        label_jflap.place(x=185, y=350)
        label_nome.place(x=2, y=300)
    campoTxt.delete("1.0", "end")
    return "break"
    
    
if __name__ == '__main__':
    try:
        sb.run(['java', '--version'], stdout=sb.DEVNULL, stderr=sb.DEVNULL)
    except FileNotFoundError:
        print('Certifique-se que o java está instalado!')
        exit()
    
    dir = os.path.dirname(os.path.dirname(__file__))
    
    caminhosJar = {
        'União': dir + "/ProjetoUniao/Uniao.jar",
        'Intersecção': dir + "/ProjetoInterseccao/Interseccao.jar",
        'Concatenação': dir + "/ProjetoConcatenacao/Concatenacao.jar",
        'Complemento': dir + "/ProjetoComplementoEstrela/ProjetoComplemento/Complemento.jar",
        'Estrela': dir + "/ProjetoComplementoEstrela/ProjetoEstrela/Estrela.jar",
        'Equivalência': "",
        'Minimização': dir + "/ProjetoMinimizacao/src/App.jar",
    }
    
    janela = Screen()
    janela.configureWindow()

    canvas = Canvas(janela.screen, bg='#1C1C1C', width=800, height=600)
    canvas.place(x=-2, y=0)

    lista_eventos = ['União', 'Intersecção', 'Concatenação', 'Complemento', 'Estrela', 'Equivalência', 'Minimização']
    lista_comandos = ['!jflap']
    comand = True
    buttons = []
    for i, evento in enumerate(lista_eventos):
        if i == 5:
            dis = 'disable'
        else:
            dis = 'normal'
        buttons.append(
            Button(
                master=canvas,
                background='#D9D9D9', 
                font=('Arial', 12, 'bold'),
                text=evento,
                width=12,
                borderwidth=4,
                height=3,
                state=dis,
                command=lambda caminho=caminhosJar[evento]: novoProcessoJava(caminho),
            )
        )
        buttons[i].place(x=28, y=50 + (i * 77))

    label_opercoes = Label(
        master=canvas,
        text='Operações:',
        font=('Arial', 14, 'bold'),
        fg='white',
        bg='#1C1C1C',
        width=11,
        height=1,
    )
    label_opercoes.place(x=25, y=12)
    frame_manipulacao = Frame(
        master=canvas,
        bg='#838080',
        width=610,
        height=790,
    )
    frame_manipulacao.place(x=190, y=2)
    frame_navbar = Frame(
        master=frame_manipulacao,
        bg='#1C1C1C',
        width=615,
        height=50,
    )
    frame_navbar.place(x=0, y=0)
    label_titulo = Label(
        master=frame_manipulacao,
        text='Autômato+',
        font=('Arial', 20, 'bold'),
        fg='white',
        bg='#1C1C1C',
        width=11,
        height=1,
    )
    label_titulo.place(x=185, y=10)
    imgs = [PhotoImage(file=os.path.join(os.path.dirname(__file__), "img", "lampada_apagada.png")), 
            PhotoImage(file=os.path.join(os.path.dirname(__file__), "img", "lampada_acesa.png"))]
    btDica = Button(
            frame_navbar,
            image=imgs[0],
            bg="#9A9999",
            borderwidth=2,
            relief="solid",
            command=lambda: dica(btDica, imgs),
        )
    btDica.place(x=568, y=5)
    
    campoTxt = Text(frame_manipulacao, width=65, height=2)
    campoTxt.bind("<Return>", conversaStark)
    campoTxt.place(x=30, y=535)
    imgs.append(PhotoImage(file=os.path.join(os.path.dirname(__file__), "img", "enviarMsg.png")))
    btEnviar = Button(
            frame_manipulacao,
            image=imgs[2],
            bg="#9A9999",
            borderwidth=2,
            relief="solid",
            command=conversaStark,
        )
    btEnviar.place(x=555, y=534)
    imgs.append(PhotoImage(file=os.path.join(os.path.dirname(__file__), "img", "alcides.png")))
    listbox = Listbox(frame_manipulacao, height=7, font=font.Font(size=11), bd=2, 
                  highlightbackground="black", highlightthickness=4, selectbackground="black")
    for item in lista_eventos:
        listbox.insert(lista_eventos.index(item), item)
    listbox.bind("<<ListboxSelect>>", listBox)

    label_nome= Label(
        master=frame_manipulacao,
        text='[A. Stark]',
        font=('Arial', 10, 'bold'),
        fg='#B60909',
        bg='#838080',
        width=18,
        height=1,
    )

    labelAlcides = Label(
        frame_manipulacao,
        image=imgs[3],
        bg="#838080",
    )

    label_jflap = Label(
        master=frame_manipulacao,
        text='- Deseja abrir o JFLAP?',
        font=('Arial', 14, 'bold'),
        fg='black',
        bg='#838080',
        width=18,
        height=1,
    )
    janela.screen.mainloop()
