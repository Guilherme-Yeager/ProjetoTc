import os
from tkinter import Button, Tk, Entry, ttk, Frame, Canvas, PhotoImage, Label, messagebox, BOTTOM, RIGHT, LEFT, BOTH, X, Y


def closeWindow(janela):
    janela.destroy()


def configureWindow(janela):
    janela.title("Informe as regras:")
    janela.resizable(False, False)
    janela.configure(bg="#BDBDBD")
    x = janela.winfo_screenwidth() // 2 - 150
    y = janela.winfo_screenheight() // 6
    janela.geometry(f"400x400+{x}+{y}")
    janela.protocol("WM_DELETE_WINDOW", lambda: closeWindow(janela))


imgs = []
canvas = None
yText = 0
text = []


def otimizarRegras(dicRegras: dict):
    chaves = list(dicRegras.keys())
    while '' in chaves:
        chaves.remove('')
        del dicRegras['']

    for chave in chaves:
        i = 1
        while (chave + str(i)) in dicRegras:
            if dicRegras[(chave + str(i))] == dicRegras[chave]:
                del dicRegras[(chave + str(i))]
            i += 1


def gerarAp():
    dicRegras = {}
    for regra in text:
        chave = regra[0].get()
        if chave in dicRegras:
            i = 1
            chave = chave[0] + str(i)
            while chave in dicRegras:
                i += 1
                chave = chave[0] + str(i)
        dicRegras[chave] = regra[1].get()
    otimizarRegras(dicRegras)


def excluirRegra(index, btExcluir, srcFrame):
    global yText
    btExcluir.destroy()
    for componente in text[index]:
        componente.destroy()
    text.pop(index)
    for componente in srcFrame.winfo_children():
        componente.grid_forget()
    yText = 0
    for componente in text:
        componente[0].grid(row=yText, column=0, padx=(40, 10))
        label = Label(srcFrame, image=imgs[0], bg='#BDBDBD')
        label.grid(row=yText, column=1, padx=(10, 10))
        componente[1].grid(row=yText, column=2, padx=(10, 0))
        btExcluir = Button(srcFrame, image=imgs[1], bg='#BDBDBD',
                           command=lambda index=yText: excluirRegra(index, btExcluir, srcFrame))
        btExcluir.grid(row=yText, column=3, padx=(10, 10))
        yText += 1
    if len(text) == 0:
        yText = 0
        return
    yText -= 1


def adicionarRegra(scrFrame):
    global yText, canvas
    if text:
        if text[yText][0].get() == '' or len(text[yText][0].get()) > 1 or not ('A' <= text[yText][0].get() <= 'Z'):
            messagebox.showinfo(title='Informação:',
                                message='Formato da regra inválida.')
            return
        yText += 1
    campoEsq = Entry(scrFrame, width=2)
    campoEsq.grid(row=yText, column=0, padx=(40, 10))
    label = Label(scrFrame, image=imgs[0], bg='#BDBDBD')
    label.grid(row=yText, column=1, padx=(10, 10))
    campoDir = Entry(scrFrame, width=30)
    campoDir.grid(row=yText, column=2, padx=(10, 0))
    text.append((campoEsq, campoDir))
    btExcluir = Button(scrFrame, image=imgs[1], bg='#BDBDBD',
                       command=lambda index=yText: excluirRegra(index, btExcluir, scrFrame))
    btExcluir.grid(row=yText, column=3, padx=(10, 10))
    canvas.update_idletasks()
    canvas.config(scrollregion=canvas.bbox("all"))
    canvas.bind("<MouseWheel>", lambda event: canvas.yview_scroll(
        int(-1*(event.delta/120)), "units"))
    canvas.yview_moveto(1.0)


def addConfigScroll():
    global canvas
    frame = Frame(janela, bg='#BDBDBD')
    frame.pack(fill=BOTH, expand=True)
    canvas = Canvas(frame, bg='#BDBDBD', highlightthickness=0)
    canvas.pack(side=LEFT, fill=BOTH, expand=True)
    scr = ttk.Scrollbar(frame, orient="vertical", command=canvas.yview)
    scr.pack(side=RIGHT, fill=Y)
    canvas.configure(yscrollcommand=scr.set)
    scrFrame = Frame(canvas, bg='#BDBDBD')
    canvas.create_window((0, 0), window=scrFrame, anchor='nw')

    def addFooter():
        btFrame = Frame(janela, bg='#BDBDBD')
        btFrame.pack(side=BOTTOM, fill=X, pady=10)
        btAdd = Button(btFrame, text="Adicionar", font=('Arial', 10, 'bold'),
                       fg='black', bg='#BDBDBD', command=lambda: adicionarRegra(scrFrame))
        btAdd.pack(side=LEFT, padx=20, pady=5)
        btGerar = Button(btFrame, text="Gerar", font=(
            'Arial', 10, 'bold'), fg='black', bg='#BDBDBD', command=gerarAp)
        btGerar.pack(side=RIGHT, padx=30, pady=5)

    addFooter()


janela = Tk()
imgs.append(PhotoImage(file=os.path.dirname(__file__) + '/img/seta.png'))
imgs.append(PhotoImage(file=os.path.dirname(__file__) + '/img/excluir.png'))
configureWindow(janela)
addConfigScroll()
janela.mainloop()
