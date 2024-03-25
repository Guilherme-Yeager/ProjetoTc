import os
from subprocess import Popen
from tkinter import Tk, Button


class Screen:

    def __init__(self) -> None:
        self.screen = Tk()

    def configureWindow(self) -> None:
        self.screen.title("Autômato+")
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

def novoProcessoJava(caminho_para_jar):
    Popen(['java', '-jar', caminho_para_jar])

if __name__ == '__main__':
    dir = os.path.dirname(os.path.dirname(__file__))

    caminhosJar = {
        'minimizacao': dir + "/ProjetoMinimizacao/src/App.jar",
    }
    
    janela = Screen()
    janela.configureWindow()

    button = Button(
                master=janela.screen,
                background='red', 
                font=('Arial', 12, 'bold'),
                text='Minimizar',
                width=10,
                height=6,
                command= lambda : novoProcessoJava(caminhosJar['minimizacao']),
            )
    button.place(x=54, y=550, anchor='center')
    
    janela.screen.mainloop()
    




