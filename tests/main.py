import subprocess
import customtkinter 
import tkinter as  tk

class Screen:

    def __init__(self) -> None:
        self.screen = tk.Tk()

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

def executar_jar(caminho_para_jar):
    subprocess.Popen(['java', '-jar', caminho_para_jar])


caminho_jar = 'D:\GitHub\ProjetoTc\Projeto\src/bin/App.jar'
janela = Screen()
janela.configureWindow()


customtkinter.set_appearance_mode("Dark")

button = customtkinter.CTkButton(master=janela.screen,
                                 fg_color=("black"), 
                                 text="Minimizar",
                                 command= lambda : executar_jar(caminho_jar))
button.place(relx=0.5, rely=0.5, anchor=tk.CENTER)

janela.screen.mainloop()
