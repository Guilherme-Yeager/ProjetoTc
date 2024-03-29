import os
import subprocess as sb
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

    def cleanWindow(self) -> None:
        for componente in self.screen.winfo_children():
            componente.destroy()

def novoProcessoJava(caminhoJar):
    try:
        sb.Popen(['java', '-jar', caminhoJar])
    except sb.CalledProcessError:
        print("Verifique a configuração do Java.")

def dica(botao, imgs):
    image = botao.cget("image")
    if image == str(imgs[0]):
        botao.config(image=imgs[1])
        botao.config(command=lambda: dica(botao, imgs))
    
    elif image == str(imgs[1]):
        botao.config(image=imgs[0])
        botao.config(command=lambda: dica(botao, imgs))

def addVingadorMaisForte():
    labelAlcides = Label(
            frame_manipulacao,
            image=imgs[3],
            bg="#838080",
        )
    labelAlcides.place(x=-15, y=332)

def isText():
    return campoTxt.get("1.0", "end-1c")

def listBox(event=None):
    print(listbox.get(listbox.curselection()))

def conversaStark(event=None):
    if(not isText() == ""):
        addVingadorMaisForte()
        campoTxt.delete("1.0", "end")
        listbox.place(x=220, y=150)
    return "break"
    
    
if __name__ == '__main__':
    try:
        sb.run(['java', '--version'], stdout=sb.DEVNULL, stderr=sb.DEVNULL)
    except FileNotFoundError:
        print('Certifique-se que o java está instalado!')
        exit()
    
    dir = os.path.dirname(os.path.dirname(__file__))
    
    caminhosJar = {
        'União': os.path.join(dir, "ProjetoUniao", "Uniao.jar"),
        'Intersecção': os.path.join(dir, "ProjetoInterseccao", "InterApp.jar"),
        'Concatenação': os.path.join(dir, "ProjetoConcatenacao", "Concatenacao.jar"),
        'Complemento': os.path.join(dir, "ProjetoComplementoEstrela", "ProjetoComplemento" , "Complemento.jar"),
        'Estrela': os.path.join(dir, "ProjetoComplementoEstrela", "ProjetoEstrela" , "Estrela.jar"),
        'Equivalência': "",
        'Minimização': os.path.join(dir, "ProjetoMinimizacao", "src", "App.jar"),
    }
    
    janela = Screen()
    janela.configureWindow()

    canvas = Canvas(janela.screen, bg='#1C1C1C', width=800, height=600)
    canvas.place(x=-2, y=0)

    lista_eventos = ['União', 'Intersecção', 'Concatenação', 'Complemento', 'Estrela', 'Equivalência', 'Minimização']
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
    janela.screen.mainloop()
