import os
from lxml import etree


def salvarAutomato(dic: dict):
    root = etree.Element("structure")
    type = etree.SubElement(root, "type")
    type.text = "pda"
    automaton = etree.SubElement(root, "automaton")
    stateInicio = etree.SubElement(automaton, "state", id='1', name='qInício')
    x = etree.SubElement(stateInicio, "x")
    x.text = '50.0'
    y = etree.SubElement(stateInicio, "y")
    y.text = '125.0'
    etree.SubElement(stateInicio, "initial")
    stateLaco = etree.SubElement(automaton, "state", id='2', name='qLaço')
    x = etree.SubElement(stateLaco, "x")
    x.text = '300.0'
    y = etree.SubElement(stateLaco, "y")
    y.text = '125.0'
    stateAceita = etree.SubElement(automaton, "state", id='3', name='qAceita')
    x = etree.SubElement(stateAceita, "x")
    x.text = '50.0'
    y = etree.SubElement(stateAceita, "y")
    y.text = '400.0'
    etree.SubElement(stateAceita, "final")
    arvore = etree.tostring(root, pretty_print=True,
                            encoding='unicode', xml_declaration=False)
    os.chdir(os.path.dirname(__file__))
    with open("automatoAp.jff", "w", encoding="utf-8") as arq:
        arq.write('<?xml version="1.0" encoding="UTF-8" standalone="no"?>\n')
        arq.write(arvore)
