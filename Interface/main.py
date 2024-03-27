import os
import subprocess as sb
from tkinter import Tk, Button, Label


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

    def cleanWindow(componente) -> None:
        for componente in componente.screen.winfo_children():
            componente.destroy()

def novoProcessoJava(caminhoJar):
    sb.Popen(['java', '-jar', caminhoJar])

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
        'Minimizacão': dir + "/ProjetoMinimizacao/src/App.jar",
    }
    
    janela = Screen()
    janela.configureWindow()
    label_eventos = Label(
                        master=janela.screen,
                        width=25,
                        height=40, 
                        background='#1C1C1C',
                    )
    label_eventos.place(x=0, y=0)
    lista_eventos = ['União', 'Intersecção', 'Concatenação', 'Complemento', 'Estrela', 'Equivalência', 'Minimizacão']
    buttons = []
    for i in range(7):
        dis = 'disable'
        if i == 6:
            dis = 'normal'
        buttons.append(
            Button(
                master=label_eventos,
                background='#D9D9D9', 
                font=('Arial', 12, 'bold'),
                text=lista_eventos[i],
                width=12,
                borderwidth=4,
                height=3,
                state=dis,
                command= lambda tipo = lista_eventos[i]: novoProcessoJava(caminhosJar[tipo]),
            )
        )
        buttons[i].place(x=87, y=98 + (i * 77), anchor='center')
    label_opercoes = Label(
                        master=label_eventos,
                        text= 'Operações:',
                        font=('Arial', 14, 'bold'),
                        fg='white',
                        width=11,
                        height=1, 
                        background='#1C1C1C',
                        )
    label_opercoes.place(x=14, y=15)
        
    janela.screen.mainloop()
    




