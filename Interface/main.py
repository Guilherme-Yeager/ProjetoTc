import os
import subprocess as sb
from tkinter import Tk, Button, Label, Canvas, PhotoImage

class Screen:
    def __init__(self) -> None:
        self.screen = Tk()

    def configureWindow(self) -> None:
        self.screen.title("Autômato+")
        self.screen.iconbitmap(os.path.dirname(__file__) + "/img/icone.ico")
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
        print("oi")
        botao.config(image=imgs[1])
        botao.config(command=lambda: dica(botao, imgs))
    
    elif image == str(imgs[1]):
        print("oioi")
        botao.config(image=imgs[0])
        botao.config(command=lambda: dica(botao, imgs))

if __name__ == '__main__':
    try:
        sb.run(['java', '--version'], stdout=sb.DEVNULL,
                stderr=sb.DEVNULL)
    except FileNotFoundError:
        print('Certifique-se que o java está instalado!')
        exit()
    
    dir = os.path.dirname(os.path.dirname(__file__))

    caminhosJar = {
        'União': "",
        'Intersecção': "",
        'Concatenação': "",
        'Complemento': "",
        'Estrela': "",
        'Equivalência': "",
        'Minimização': dir + "/ProjetoMinimizacao/src/App.jar",
    }
    
    janela = Screen()
    janela.configureWindow()

    canvas = Canvas(janela.screen, bg='#1C1C1C')
    canvas.pack(side='left', fill='both', expand=True)

    lista_eventos = ['União', 'Intersecção', 'Concatenação', 'Complemento', 'Estrela', 'Equivalência', 'Minimização']
    buttons = []
    for i in range(7):
        dis = 'disable' if i != 6 else 'normal'
        buttons.append(
            Button(
                master=canvas,
                background='#D9D9D9', 
                font=('Arial', 12, 'bold'),
                text=lista_eventos[i],
                width=12,
                borderwidth=4,
                height=3,
                state=dis,
                command=lambda caminho=lista_eventos[i]: novoProcessoJava(caminhosJar[caminho]),
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
    label_manipulacao = Label(
        master=canvas,
        bg='#E7E5E5',
        width=450,
        height=300,
    )
    label_manipulacao.place(x=190, y=0)
    imgs = [PhotoImage(file=os.path.dirname(__file__) + "/img/lampada_apagada.png"), PhotoImage(file=os.path.dirname(__file__) + "/img/lampada_acesa.png")]
    btDica = Button(
            label_manipulacao,
            image=imgs[0],
            bg="#9A9999",
            borderwidth=2,
            relief="solid",
            command=lambda: dica(btDica, imgs),
        )
    btDica.place(x=568, y=0)
    janela.screen.mainloop()