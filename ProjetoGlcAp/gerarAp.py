import os
from lxml import etree


def salvarAutomato(dic: dict):
    root = etree.Element("structure")
    etree.SubElement(root, "type").text = "pda"
    automaton = etree.SubElement(root, "automaton")
    stateInicio = etree.SubElement(automaton, "state", id='1', name='qInício')
    etree.SubElement(stateInicio, "x").text = '50.0'
    etree.SubElement(stateInicio, "y").text = '125.0'
    etree.SubElement(stateInicio, "initial")
    stateLaco = etree.SubElement(automaton, "state", id='2', name='qLaço')
    etree.SubElement(stateLaco, "x").text = '300.0'
    etree.SubElement(stateLaco, "y").text = '125.0'
    stateAceita = etree.SubElement(automaton, "state", id='3', name='qAceita')
    etree.SubElement(stateAceita, "x").text = '50.0'
    etree.SubElement(stateAceita, "y").text = '400.0'
    etree.SubElement(stateAceita, "final")
    ids = 4
    for chave, valor in dic.items():
        state = etree.SubElement(
            automaton, "state", id=str(ids), name='q' + str(ids))
        etree.SubElement(state, "x").text = str(ids * 50)
        etree.SubElement(state, "y").text = str(200 + ids * 5)
        ids += 1

    arvore = etree.tostring(root, pretty_print=True,
                            encoding='unicode', xml_declaration=False)
    os.chdir(os.path.dirname(__file__))
    with open("automatoAp.jff", "w", encoding="utf-8") as arq:
        arq.write('<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n')
        arq.write(arvore)


salvarAutomato({'A': '*', 'A1': '/', 'A2': '+', 'A3': '-',
               'A4': 'ABA', 'B': '1', 'B1': '2'})
