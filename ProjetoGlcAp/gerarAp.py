import os
from lxml import etree


def addTerminal(terminais, terminal):
    if isinstance(terminal, str):
        if terminal != '' and terminal.islower():
            terminais.add(terminal)
        else:
            terminais.add(terminal)


def salvarAutomato(dic: dict, s):
    root = etree.Element("structure")
    etree.SubElement(root, "type").text = "pda"
    automaton = etree.SubElement(root, "automaton")
    stateInicio = etree.SubElement(automaton, "state", id='0', name='qInício')
    etree.SubElement(stateInicio, "x").text = '50.0'
    etree.SubElement(stateInicio, "y").text = '125.0'
    etree.SubElement(stateInicio, "initial")
    stateLaco = etree.SubElement(automaton, "state", id='1', name='qLaço')
    etree.SubElement(stateLaco, "x").text = '600.0'
    etree.SubElement(stateLaco, "y").text = '125.0'
    stateAceita = etree.SubElement(automaton, "state", id='2', name='qAceita')
    etree.SubElement(stateAceita, "x").text = '50.0'
    etree.SubElement(stateAceita, "y").text = '400.0'
    etree.SubElement(stateAceita, "final")
    ids = 3
    for _, valor in dic.items():        # Adicionando estados
        deveCriarEstados = False
        for c in valor:
            if (isinstance(c, str) and c.isupper()):
                deveCriarEstados = True
        if (deveCriarEstados):
            for _ in range(1, len(valor)):
                state = etree.SubElement(
                    automaton, "state", id=str(ids), name='q' + str(ids))
                etree.SubElement(state, "x").text = str(ids * 50)
                etree.SubElement(state, "y").text = str(200 + ids * 5)
                ids += 1
    listaChaves = sorted(list(dic.keys()))          # Adicionando transições
    listaRegraStart = []
    i = 1
    while s in dic:
        listaRegraStart.append(s)
        s = s + str(i)
        i += 1
    listaChaves = listaRegraStart + \
        [item for item in listaChaves if item not in listaRegraStart]
    transition = etree.SubElement(
        automaton, "transition")
    etree.SubElement(transition, "from").text = '0'
    etree.SubElement(transition, "to").text = '1'
    etree.SubElement(transition, "read").text = ''
    etree.SubElement(transition, "pop").text = ''
    etree.SubElement(transition, "push").text = listaChaves[0][0]
    transition = etree.SubElement(
        automaton, "transition")
    etree.SubElement(transition, "from").text = '1'
    etree.SubElement(transition, "to").text = '2'
    etree.SubElement(transition, "read").text = ''
    etree.SubElement(transition, "pop").text = 'Z'
    etree.SubElement(transition, "push")
    ids = 3
    terminais = set()
    while True:
        if listaChaves == []:
            break
        regraAtual = listaChaves[0]
        i = 1
        listaRegrasAtuais = []
        while regraAtual in listaChaves:
            listaRegrasAtuais.append(regraAtual)
            regraAtual = regraAtual + str(i)
            i += 1
        listaChaves = [
            item for item in listaChaves if item not in listaRegrasAtuais]
        for regra in listaRegrasAtuais:
            if 0 <= len(dic[regra]) <= 1:
                transition = etree.SubElement(automaton, "transition")
                etree.SubElement(transition, "from").text = '1'
                etree.SubElement(transition, "to").text = '1'
                etree.SubElement(transition, "read")
                etree.SubElement(
                    transition, "pop").text = regra[0]
                etree.SubElement(transition, "push").text = dic[regra]
                addTerminal(terminais=terminais, terminal=dic[regra])
                continue
            if len(dic[regra]) > 1:
                transition = etree.SubElement(automaton, "transition")
                etree.SubElement(transition, "from").text = '1'
                etree.SubElement(transition, "to").text = str(ids)
                etree.SubElement(transition, "read")
                etree.SubElement(
                    transition, "pop").text = regra[0]
            for index, derivacao in enumerate(dic[regra][::-1]):
                addTerminal(terminais=terminais, terminal=derivacao)
                if derivacao != '' and index == 0:
                    etree.SubElement(transition, "push").text = derivacao
                    continue
                if len(dic[regra]) > 1:
                    transition = etree.SubElement(automaton, "transition")
                    etree.SubElement(transition, "from").text = str(ids)
                    etree.SubElement(transition, "to").text = str(
                        ids + 1) if not (index + 1 == len(dic[regra])) else '1'
                    etree.SubElement(transition, "read")
                    etree.SubElement(transition, "pop")
                    etree.SubElement(transition, "push").text = derivacao
                    ids += 1
    for terminal in terminais:
        transition = etree.SubElement(automaton, "transition")
        etree.SubElement(transition, "from").text = '1'
        etree.SubElement(transition, "to").text = '1'
        etree.SubElement(transition, "read").text = terminal
        etree.SubElement(
            transition, "pop").text = terminal
        etree.SubElement(transition, "push")
        continue
    arvore = etree.tostring(root, pretty_print=True,
                            encoding='unicode', xml_declaration=False)
    with open(os.path.dirname(os.path.dirname(__file__)) + "/automatoAp.jff", "w", encoding="utf-8") as arq:
        arq.write('<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n')
        arq.write(arvore)
