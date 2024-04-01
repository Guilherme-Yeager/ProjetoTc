import os
import threading
import subprocess as sb
from tkinter import Tk, Button, Label, Canvas, PhotoImage, Frame, Text

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
        self.screen.protocol("WM_DELETE_WINDOW", self.closeWindow)

    def closeWindow(self) -> None:
        global th_aux
        self.screen.destroy()
        th_aux = False

    def cleanWindow(self, frame, filtro=None) -> None:
        global comand
        comand = True
        for componente in frame.winfo_children():
            if componente in filtro:
                componente.place_forget()


def habilitarComponentes():
    for bt in buttons_operacoes:
        bt.config(state="normal")
    janela.cleanWindow(frame_manipulacao, [labelAlcides, label_nome, label_mensagem])

def thExecutar():
    global processo
    while processo.poll() is None:
        ...
    if th_aux:
        janela.screen.after(0, habilitarComponentes)

def novoProcessoJava(caminhoJar):
    global processo, thread
    try:
        processo = sb.Popen(['java', '-jar', caminhoJar], creationflags=sb.CREATE_NO_WINDOW)
        if caminhoJar.split("/")[-1] == "JFLAP.jar":
            return
        for bt in buttons_operacoes:
            bt.config(state="disable")
        addVingadorMaisForte()
        label_mensagem.place(x=185, y=350)
        thread = threading.Thread(target=thExecutar)
        thread.start()
    except sb.CalledProcessError:
        print("Verifique a configuração do Java.")

def dica(botao, imgs):
    global comand
    image = botao.cget("image")
    if comand and image == str(imgs[0]):
        botao.config(image=imgs[1])
        botao.config(command=lambda: dica(botao, imgs))
        comand = False
        janela_dica = Tk()
        janela_dica.title("Dicas")
        janela_dica.iconbitmap(dir + "/Interface/img/icone.ico")
        janela_dica.resizable(False, False)
        janela_dica.configure(bg="#1C1C1C")
        x = janela_dica.winfo_screenwidth() // 2 - 100
        y = janela_dica.winfo_screenheight() // 4
        janela_dica.geometry(f"350x350+{x}+{y}")
        label = Label(
                    janela_dica, text="\nAutômato+ realiza operações\n com autômatos.\n\nComandos:\n\n!JFLAP",
                    font=('Arial', 12, 'bold'),
                    fg='white',
                    bg='#1C1C1C',
                )
        label.pack()
        janela_dica.protocol("WM_DELETE_WINDOW", lambda : (dica(botao, imgs), janela_dica.destroy()))
        janela_dica.mainloop()
    elif image == str(imgs[1]):
        botao.config(image=imgs[0])
        botao.config(command=lambda: dica(botao, imgs))
        comand = True

def addVingadorMaisForte():
    labelAlcides.place(x=-15, y=332)
    label_nome.place(x=2, y=300)

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


def conversaStark(event=None):
    global comand
    if(comand and (isText().lower() in lista_comandos)):
        addVingadorMaisForte()
        if isText().lower() == "!jflap":
            addButtonJflap()
            label_jflap.place(x=185, y=350)
        comand = False
    campoTxt.delete("1.0", "end")
    return "break"
    
    
if __name__ == '__main__':
    try:
        sb.run(['java', '--version'], stdout=sb.DEVNULL, stderr=sb.DEVNULL)
    except FileNotFoundError:
        print('Certifique-se que o java está instalado!')
        exit()
    
    processo = None
    thread = None
    th_aux = True

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
    canvas.place(x=-2, y=-2)

    lista_eventos = ['União', 'Intersecção', 'Concatenação', 'Complemento', 'Estrela', 'Equivalência', 'Minimização']
    lista_comandos = ['!jflap']
    comand = True
    buttons_operacoes = []
    for i, evento in enumerate(lista_eventos):
        buttons_operacoes.append(
            Button(
                master=canvas,
                background='#D9D9D9', 
                font=('Arial', 12, 'bold'),
                text=evento,
                width=12,
                borderwidth=4,
                height=3,
                state="normal",
                command=lambda caminho=caminhosJar[evento]: novoProcessoJava(caminho),
            )
        )
        buttons_operacoes[i].place(x=28, y=50 + (i * 77))

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
    label_mensagem = Label(
                    master=frame_manipulacao,
                    text='- Aguarde a operação \nser concluída.',
                    font=('Arial', 14, 'bold'),
                    fg='black',
                    bg='#838080',
                    width=18,
                    height=2,
                )
    janela.screen.mainloop()
    if thread is not None:
        thread.join()