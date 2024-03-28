import os
import subprocess as sb
from tkinter import Tk, Button, Label, Canvas, PhotoImage, Frame, Spinbox

class Screen:
    def __init__(self) -> None:
        self.screen = Tk()

    def configureWindow(self) -> None:
        self.screen.title("Autômato+")
        self.screen.iconbitmap(os.path.join(os.path.dirname(__file__), "img", "icone.ico"))
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

if __name__ == '__main__':
    try:
        sb.run(['java', '--version'], stdout=sb.DEVNULL, stderr=sb.DEVNULL)
    except FileNotFoundError:
        print('Certifique-se que o java está instalado!')
        exit()
    
    dir = os.path.dirname(os.path.dirname(__file__))
    print(dir)
    caminhosJar = {
        'União': "",
        'Intersecção': "",
        'Concatenação': os.path.join(dir, "ProjetoConcatenacao", "Concatenacao.jar"),
        'Complemento': "",
        'Estrela': "",
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
        dis = ''
        if i == 6 or i == 2:
            dis = 'normal'
        else:
            dis = 'disable'
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
        bg='#E7E5E5',
        width=610,
        height=800,
    )
    frame_manipulacao.place(x=190, y=0)
    frame_navbar = Frame(
        master=frame_manipulacao,
        bg='#1C1C1C',
        width=615,
        height=50,
    )
    frame_navbar.place(x=-1, y=2)
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
    # valores = ['Dica']
    # valores.extend(lista_eventos)
    # spinbox = Spinbox(master=frame_navbar, values=valores, width=max(len(valor) for valor in lista_eventos) + 2)
    # spinbox.place(x=450, y=15)

    janela.screen.mainloop()
