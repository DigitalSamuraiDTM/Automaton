from pathlib import Path
from typing import Any
import yaml

W, H = 1920, 1080
ROOT = Path(__file__).resolve().parent

LIGHT = {
    "theme": "light", "bg": "#FFFFFF", "aside": "#FFFFFF", "float": "#FFFFFF",
    # Непрозрачные эквиваленты text-primary 85%, secondary 50%, hint 30% на белом фоне.
    "text": "#262626", "secondary": "#808080", "hint": "#B3B3B3",
    "line": "#0000001A", "lineStrong": "#00000026", "generic": "#0000000D",
    "genericHover": "#00000015", "selection": "#FF003D26", "brand": "#FF003D",
    "brandHover": "#E9033A", "brandText": "#FFFFFF", "brandHeavy": "#BD0935",
    "positive": "#32BA7626", "positiveText": "#2D8B5D", "warning": "#FFBE5C4D",
    "warningText": "#8A5A18", "danger": "#FF003D26", "dangerText": "#BD0935",
    "dangerSolid": "#E9033A", "dangerLine": "#FF003DCC",
    "info": "#527CCD26", "infoText": "#355CA8", "misc": "#6B849926", "miscText": "#556575",
    "code": "#FAFAFA", "grid": "#0000000D", "shell": "#E5E5E5", "shellDark": "#383838", "robotFace": "#262626",
}
DARK = {
    "theme": "dark", "bg": "#221D22", "aside": "#1D191D", "float": "#2D282D",
    # Непрозрачные эквиваленты текстовых токенов на базовом тёмном фоне #221D22.
    "text": "#DEDDDE", "secondary": "#918E91", "hint": "#646264",
    "line": "#FFFFFF26", "lineStrong": "#FFFFFF40", "generic": "#FFFFFF1A",
    "genericHover": "#FFFFFF26", "selection": "#E5325D33", "brand": "#E5325D",
    "brandHover": "#E8476D", "brandText": "#FFFFFF", "brandHeavy": "#ED708E",
    "positive": "#4DB09B26", "positiveText": "#82C8B9", "warning": "#FFBE5C33",
    "warningText": "#FFD28D", "danger": "#E5325D33", "dangerText": "#ED708E",
    "dangerSolid": "#E8476D", "dangerLine": "#BE2E51CC",
    "info": "#7B9BDB26", "infoText": "#A9BEE8", "misc": "#60809C33", "miscText": "#8099B0",
    "code": "#1B171B", "grid": "#FFFFFF12", "shell": "#BDBBBD", "shellDark": "#433F43", "robotFace": "#211D21",
}


def solid(color: str) -> dict[str, str]:
    """Создаёт заливку. @param color HEX-цвет. @return Объект заливки PPTD."""
    return {"type": "solid", "color": color}


def radius_for(w: float, h: float, radius: float) -> int:
    """Переводит радиус px в параметр roundRect. @param w,h Размеры. @param radius Радиус px. @return Коэффициент PPTD."""
    return min(50000, max(0, int(radius / min(w, h) * 100000)))


def box(eid: str, x: float, y: float, w: float, h: float, color: str,
        radius: float = 0, border: str | None = None, border_width: int = 1,
        rotation: float = 0, opacity: float = 1) -> dict[str, Any]:
    """Создаёт фигуру. @param eid ID. @param x,y,w,h Геометрия. @param color Заливка. @param radius Радиус px. @param border Контур. @param border_width Толщина. @param rotation Поворот. @param opacity Прозрачность. @return Элемент shape."""
    result: dict[str, Any] = {"elementId": eid, "elementType": "shape", "bounds": [round(x, 2), round(y, 2), round(w, 2), round(h, 2)],
                              "shapeName": "roundRect" if radius else "rect", "fill": solid(color), "opacity": opacity}
    if radius:
        result["adjustments"] = [radius_for(w, h, radius)]
    if border:
        result["border"] = {"style": "solid", "width": border_width, "color": border}
    if rotation:
        result["rotation"] = rotation
    return result


def txt(eid: str, x: float, y: float, w: float, h: float, value: str, p: dict[str, str],
        size: int = 13, color: str | None = None, bold: bool = False,
        align: str = "left", valign: str = "middle", mono: bool = False,
        wrap: bool = False, line: int | None = None) -> dict[str, Any]:
    """Создаёт текст по шкале Gravity UI. @param eid ID. @param x,y,w,h Геометрия. @param value Текст. @param p Палитра. @param size Размер. @param color Цвет. @param bold Вес 600. @param align Выравнивание. @param valign Вертикальное выравнивание. @param mono Menlo. @param wrap Перенос. @param line Межстрочный интервал px. @return Элемент text."""
    default_line = 18 if size == 13 else (20 if size == 15 else (24 if size in (17, 20) else (28 if size == 24 else 16)))
    return {"elementId": eid, "elementType": "text", "bounds": [x, y, w, h],
            "content": {"text": value, "fontSize": size, "fontFamily": "Menlo" if mono else "Inter",
                        "color": color or p["text"], "bold": bold, "align": [align, valign],
                        "wrap": wrap, "lineHeightPx": line or default_line}}


def ico(eid: str, x: float, y: float, size: float, name: str, color: str) -> dict[str, Any]:
    """Создаёт интерфейсную иконку. @param eid ID. @param x,y,size Геометрия. @param name Имя. @param color Цвет. @return Элемент icon."""
    return {"elementId": eid, "elementType": "icon", "bounds": [x, y, size, size], "iconName": name, "fill": solid(color)}


def image(eid: str, x: float, y: float, w: float, h: float, src: str) -> dict[str, Any]:
    """Вставляет знак Automaton. @param eid ID. @param x,y,w,h Геометрия. @param src Путь. @return Элемент image."""
    return {"elementId": eid, "elementType": "image", "bounds": [x, y, w, h], "src": src, "fit": {"mode": "contain"}}


def connector(eid: str, x1: float, y1: float, x2: float, y2: float, color: str,
              width: int = 1, arrow: bool = False, dash: bool = False) -> dict[str, Any]:
    """Создаёт соединительную линию. @param eid ID. @param x1,y1 Начало. @param x2,y2 Конец. @param color Цвет. @param width Толщина. @param arrow Стрелка. @param dash Пунктир. @return Элемент line."""
    x, y = min(x1, x2), min(y1, y2)
    w, h = max(1, abs(x2 - x1)), max(1, abs(y2 - y1))
    return {"elementId": eid, "elementType": "line", "bounds": [x, y, w, h], "viewBox": [w, h],
            "points": f"{x1-x},{y1-y} {x2-x},{y2-y}", "curve": "round", "arrow": [None, "arrow"] if arrow else [None, None],
            "border": {"style": "dash" if dash else "solid", "width": width, "color": color}}


def card(elements: list, eid: str, x: float, y: float, w: float, h: float,
         p: dict[str, str], large: bool = True, selected: bool = False) -> None:
    """Добавляет Gravity Card. @param elements Список. @param eid ID. @param x,y,w,h Геометрия. @param p Палитра. @param large Размер L с радиусом 16. @param selected Выделение brand 2 px. @return None."""
    elements.append(box(eid, x, y, w, h, p["float"], 16 if large else 8, p["brand"] if selected else p["line"], 2 if selected else 1))


def button(elements: list, eid: str, x: float, y: float, w: float, label: str,
           p: dict[str, str], view: str = "normal", icon_name: str | None = None,
           radius: float = 8) -> None:
    """Добавляет Gravity Button L. @param elements Список. @param eid ID. @param x,y,w Геометрия. @param label Подпись. @param p Палитра. @param view Стиль кнопки Gravity. @param icon_name Иконка. @param radius Радиус: 18 соответствует circle-circle. @return None."""
    if view == "action":
        bg, fg, border = p["brand"], p["brandText"], None
    elif view == "outlined":
        bg, fg, border = "#00000000", p["text"], p["line"]
    elif view == "outlined-danger":
        bg, fg, border = "#00000000", p["dangerSolid"], p["dangerLine"]
    elif view == "flat":
        bg, fg, border = "#00000000", p["secondary"], None
    else:
        bg, fg, border = p["generic"], p["text"], None
    elements.append(box(eid + "-bg", x, y, w, 36, bg, radius, border))
    if icon_name:
        text_w = min(w - 64, max(24, round(len(label) * 7.1)))
        group_x = x + (w - text_w - 24) / 2
        elements.append(ico(eid + "-icon", group_x, y + 10, 16, icon_name, fg))
        elements.append(txt(eid + "-text", group_x + 24, y, text_w, 36, label, p, 13, fg, line=36))
    else:
        elements.append(txt(eid + "-text", x + 16, y, w - 32, 36, label, p, 13, fg, align="center", line=36))


def icon_button(elements: list, eid: str, x: float, y: float, icon_name: str, p: dict[str, str]) -> None:
    """Добавляет Gravity Button M в варианте icon-only. @param elements Список. @param eid ID. @param x,y Позиция. @param icon_name Иконка. @param p Палитра. @return None."""
    elements.append(box(eid + "-bg", x, y, 28, 28, "#00000000", 6))
    elements.append(ico(eid + "-icon", x + 6, y + 6, 16, icon_name, p["secondary"]))


def label(elements: list, eid: str, x: float, y: float, w: float, value: str,
          p: dict[str, str], theme: str = "normal", close: bool = False,
          size: str = "s") -> None:
    """Добавляет Gravity Label. @param elements Список. @param eid ID. @param x,y,w Геометрия. @param value Текст. @param p Палитра. @param theme Семантическая тема. @param close Кнопка удаления. @param size Размер xxs, xs, s или m. @return None."""
    mapping = {"normal": (p["misc"], p["miscText"]), "success": (p["positive"], p["positiveText"]),
               "warning": (p["warning"], p["warningText"]), "danger": (p["danger"], p["dangerText"]),
               "info": (p["info"], p["infoText"])}
    sizes = {"xxs": (16, 4, 6), "xs": (20, 4, 8), "s": (24, 6, 10), "m": (28, 8, 12)}
    h, radius, margin = sizes[size]
    bg, fg = mapping[theme]
    elements.append(box(eid + "-bg", x, y, w, h, bg, radius))
    elements.append(txt(eid + "-text", x + margin, y, w - (h + margin if close else 2 * margin), h,
                        value, p, 13, fg, align="center", line=16))
    if close:
        elements.append(box(eid + "-close-addon", x + w - h, y, h, h, "#00000000", radius))
        elements.append(ico(eid + "-close", x + w - h + (h - 14) / 2, y + (h - 14) / 2, 14, "fas:xmark", fg))


def input_control(elements: list, eid: str, x: float, y: float, w: float, value: str,
                  p: dict[str, str], icon_name: str | None = None, select: bool = False,
                  mono: bool = False, placeholder: bool = False) -> None:
    """Добавляет TextInput или Select L. @param elements Список. @param eid ID. @param x,y,w Геометрия. @param value Значение. @param p Палитра. @param icon_name Иконка слева. @param select Признак Select. @param mono Menlo. @param placeholder Подсказка вместо введённого значения. @return None."""
    elements.append(box(eid + "-bg", x, y, w, 36, "#00000000", 8, p["line"]))
    left = x + 12
    if icon_name:
        elements.append(ico(eid + "-icon", left, y + 10, 16, icon_name, p["secondary"]))
        left += 28
    value_color = p["hint"] if placeholder or not value else p["text"]
    elements.append(txt(eid + "-value", left, y, w - (left - x) - (34 if select else 12), 36,
                        value, p, 13, value_color, mono=mono, line=16))
    if select:
        elements.append(ico(eid + "-chevron", x + w - 26, y + 12, 12, "fas:chevron-down", p["secondary"]))


def switch(elements: list, eid: str, x: float, y: float, enabled: bool, p: dict[str, str]) -> None:
    """Добавляет Gravity Switch M. @param elements Список. @param eid ID. @param x,y Позиция. @param enabled Состояние. @param p Палитра. @return None."""
    elements.append(box(eid + "-track", x, y, 32, 18, p["brand"] if enabled else p["lineStrong"], 9))
    elements.append(box(eid + "-thumb", x + (16 if enabled else 2), y + 2, 14, 14, "#FFFFFF", 7))


def checkbox(elements: list, eid: str, x: float, y: float, checked: bool, p: dict[str, str],
             indeterminate: bool = False) -> None:
    """Добавляет Gravity Checkbox M для таблицы. @param elements Список. @param eid ID. @param x,y Позиция. @param checked Выбрано. @param p Палитра. @param indeterminate Частичный выбор. @return None."""
    marked = checked or indeterminate
    elements.append(box(eid + "-bg", x, y, 14, 14, p["brand"] if marked else "#00000000", 3,
                        p["brand"] if marked else p["lineStrong"]))
    if indeterminate:
        elements.append(box(eid + "-minus", x + 3, y + 6, 8, 2, "#FFFFFF", 1))
    elif checked:
        elements.append(ico(eid + "-check", x + 3, y + 3, 8, "fas:check", "#FFFFFF"))


def pagination(elements: list, eid: str, x: float, y: float, p: dict[str, str]) -> None:
    """Добавляет Gravity Pagination M для единственной страницы. @param elements Список. @param eid ID. @param x,y Позиция. @param p Палитра. @return None."""
    for suffix, offset, icon_name in [("previous", 0, "fas:chevron-left"), ("next", 64, "fas:chevron-right")]:
        elements.append(box(f"{eid}-{suffix}", x + offset, y, 28, 28, "#00000000", 6, p["line"]))
        elements.append(ico(f"{eid}-{suffix}-icon", x + offset + 7, y + 7, 14, icon_name, p["hint"]))
    elements.append(box(eid + "-current", x + 32, y, 28, 28, p["selection"], 6))
    elements.append(txt(eid + "-current-text", x + 32, y, 28, 28, "1", p, 13, p["brandHeavy"], align="center", line=16))
    elements.append(box(eid + "-page-size", x + 104, y, 140, 28, "#00000000", 6, p["line"]))
    elements.append(txt(eid + "-page-size-text", x + 116, y, 100, 28, "7 на странице", p, 13, p["text"], line=16))
    elements.append(ico(eid + "-page-size-icon", x + 220, y + 8, 12, "fas:chevron-down", p["secondary"]))


def segmented(elements: list, eid: str, x: float, y: float, w: float, options: list[str],
              selected: str, p: dict[str, str], icon_names: list[str | None] | None = None) -> None:
    """Добавляет контурный Gravity SegmentedRadioGroup L. @param elements Список. @param eid ID. @param x,y,w Геометрия. @param options Взаимоисключающие значения. @param selected Выбранное значение. @param p Палитра. @param icon_names Иконки опций. @return None."""
    cell_w = w / len(options)
    selected_index = options.index(selected)
    endpoint_selected = selected_index in (0, len(options) - 1)
    elements.append(box(eid + "-border", x, y, w, 36, "#00000000", 8,
                        p["brand"] if endpoint_selected else p["line"]))
    selected_x = x + selected_index * cell_w
    if endpoint_selected:
        elements.append(box(eid + "-current", selected_x, y, cell_w, 36, p["selection"], 8))
        square_x = selected_x + cell_w - 8 if selected_index == 0 else selected_x
        elements.append(box(eid + "-current-square", square_x, y + 1, 8, 34, p["selection"]))
        divider_x = selected_x + cell_w if selected_index == 0 else selected_x
        elements.append(connector(eid + "-current-border", divider_x, y, divider_x, y + 36, p["brand"]))
    else:
        elements.append(box(eid + "-current", selected_x, y, cell_w, 36, p["selection"], 0, p["brand"]))
    for i in range(1, len(options)):
        if i not in (selected_index, selected_index + 1):
            divider_x = x + i * cell_w
            elements.append(connector(f"{eid}-divider-{i}", divider_x, y + 1, divider_x, y + 35, p["line"]))
    for i, option in enumerate(options):
        cell_x = x + i * cell_w
        active = i == selected_index
        color = p["brandHeavy"] if active else p["secondary"]
        icon_name = icon_names[i] if icon_names else None
        if icon_name:
            text_w = max(24, round(len(option) * 7.1))
            content_w = 24 + text_w
            content_x = cell_x + (cell_w - content_w) / 2
            elements.append(ico(f"{eid}-icon-{i}", content_x, y + 10, 16, icon_name, color))
            elements.append(txt(f"{eid}-text-{i}", content_x + 24, y, text_w + 4, 36, option, p, 13, color))
        else:
            elements.append(txt(f"{eid}-text-{i}", cell_x + 8, y, cell_w - 16, 36, option, p, 13, color, align="center"))


def tabs(elements: list, items: list[tuple[str, float, float, bool]], y: float, p: dict[str, str]) -> None:
    """Добавляет Gravity Tabs L с активным подчёркиванием. @param elements Список. @param items Название, x, ширина и активность. @param y Позиция. @param p Палитра. @return None."""
    elements.append(box("tabs-line", 264, y + 39, 1632, 1, p["line"]))
    for i, (name, x, w, active) in enumerate(items):
        elements.append(txt(f"tab-{i}", x, y, w, 40, name, p, 15,
                            p["text"] if active else p["secondary"], False, "center", line=20))
        if active:
            elements.append(box("tab-current", x, y + 38, w, 2, p["brand"]))


def user_label(elements: list, x: float, y: float, p: dict[str, str]) -> None:
    """Добавляет Gravity UserLabel для текущего аккаунта. @param elements Список. @param x,y Позиция. @param p Палитра. @return None."""
    elements.append(box("account-avatar", x, y, 32, 32, p["generic"], 16))
    elements.append(txt("account-initials", x, y, 32, 32, "АН", p, 11, p["text"], True, "center"))
    elements.append(txt("account-name", x + 44, y, 160, 32, "Андрей Н.", p, 13))


def alert(elements: list, eid: str, x: float, y: float, w: float, title: str,
          description: str, p: dict[str, str]) -> None:
    """Добавляет Gravity Alert Success с вертикальным текстом. @param elements Список. @param eid ID. @param x,y,w Геометрия. @param title Заголовок. @param description Описание. @param p Палитра. @return None."""
    elements.append(box(eid + "-bg", x, y, w, 96, p["positive"], 8))
    elements.append(ico(eid + "-icon", x + 20, y + 18, 16, "fas:circle-check", p["positiveText"]))
    elements.append(txt(eid + "-title", x + 48, y + 10, w - 68, 24, title, p, 13, p["positiveText"], bold=True))
    elements.append(txt(eid + "-copy", x + 20, y + 42, w - 40, 40, description, p, 11, p["secondary"], wrap=True, line=16))


def field(elements: list, eid: str, x: float, y: float, w: float, caption: str,
          value: str, p: dict[str, str], mono: bool = False, select: bool = False) -> None:
    """Добавляет TextInput или Select с подписью. @param elements Список. @param eid ID. @param x,y,w Геометрия. @param caption Подпись. @param value Значение. @param p Палитра. @param mono Menlo. @param select Предопределённые значения. @return None."""
    elements.append(txt(eid + "-caption", x, y, w, 18, caption, p, 13, p["secondary"]))
    input_control(elements, eid + "-input", x, y + 24, w, value, p, select=select, mono=mono)


def breadcrumbs(elements: list, active: str, p: dict[str, str], leaf: str | None = None) -> None:
    """Создаёт Gravity Breadcrumbs с переключателем проекта. @param elements Список. @param active Текущий раздел. @param p Палитра. @param leaf Страница внутри раздела. @return None."""
    if active == "Настройки":
        items = ["Automaton", active]
    else:
        items = ["Automaton", "Neuro Photo / Mobile", active]
        if leaf:
            items.append(leaf)
    x = 264
    for i, name in enumerate(items):
        current = i == len(items) - 1
        project_switch = name == "Neuro Photo / Mobile"
        width = max(64, round(len(name) * 7.1 + (22 if project_switch else 0)))
        elements.append(txt(f"breadcrumb-{i}", x, 0, width - (18 if project_switch else 0), 64, name, p, 13,
                            p["text"] if current else p["secondary"]))
        if project_switch:
            elements.append(ico("breadcrumb-project-chevron", x + width - 20, 24, 16, "fas:chevron-down", p["secondary"]))
        x += width
        if not current:
            elements.append(txt(f"breadcrumb-separator-{i}", x, 0, 24, 64, "/", p, 13, p["hint"], align="center"))
            x += 24


def shell(p: dict[str, str], active: str, leaf: str | None = None) -> dict[str, Any]:
    """Создаёт двухуровневый Gravity AsideHeader. @param p Палитра. @param active Текущий пункт. @param leaf Вложенная страница. @return Страница с общими и контекстными действиями."""
    e: list[dict[str, Any]] = []
    e.append(box("aside", 0, 0, 240, H, p["aside"]))
    e.append(box("aside-divider", 239, 0, 1, H, p["line"]))
    e.append(image("brand-mark", 16, 14, 22, 34, f"media/logo-{p['theme']}.png"))
    e.append(txt("brand", 48, 14, 150, 34, "AUTOMATON", p, 15, bold=True))
    e.append(ico("collapse", 208, 23, 16, "fas:table-columns", p["secondary"]))
    top_nav = [("Функции", "fas:table-cells-large"), ("Поиск", "fas:magnifying-glass")]
    for i, (name, icon_name) in enumerate(top_nav):
        y = 84 + i * 44
        e.append(ico(f"nav-icon-{i}", 20, y + 11, 18, icon_name, p["text"]))
        e.append(txt(f"nav-text-{i}", 52, y, 172, 40, name, p, 13))
    e.append(box("aside-section-line", 16, 184, 208, 1, p["line"]))
    if active == "Автотесты":
        secondary_nav = [("Тест-кейсы", "fas:list-check"), ("Автотесты", "fas:robot"),
                         ("Регресс", "fas:arrows-rotate"), ("Покрытие", "fas:chart-pie")]
    elif active == "Настройки проекта":
        secondary_nav = [("Интеграции", "fas:plug"), ("Настройки проекта", "fas:gear")]
    else:
        secondary_nav = [("Главная", "fas:house"), ("Права доступа", "fas:user-lock"),
                         ("Операции", "fas:clock-rotate-left"), ("Состояние системы", "fas:heart-pulse"),
                         ("Документация", "fas:book")]
    for i, (name, icon_name) in enumerate(secondary_nav):
        y = 208 + i * 44
        current = name == "Тест-кейсы" if active == "Автотесты" else name == active
        if current:
            e.append(box(f"subnav-current-{i}", 8, y, 224, 40, p["selection"], 6))
        color = p["brandHeavy"] if current else p["text"]
        e.append(ico(f"subnav-icon-{i}", 20, y + 11, 18, icon_name, color))
        e.append(txt(f"subnav-text-{i}", 52, y, 172, 40, name, p, 13, color))
    if active == "Автотесты":
        button(e, "create-test-sidebar", 16, 400, 208, "Создать", p, "action", "fas:plus", radius=18)
    footer_items = [("Настройки", "fas:gear", 900), ("Помощь", "fas:circle-question", 944)]
    for i, (name, icon_name, y) in enumerate(footer_items):
        current = name == active
        if current:
            e.append(box(f"footer-current-{i}", 8, y, 224, 40, p["selection"], 6))
        color = p["brandHeavy"] if current else p["secondary"]
        e.append(ico(f"footer-icon-{i}", 20, y + 11, 18, icon_name, color))
        e.append(txt(f"footer-text-{i}", 52, y, 172, 40, name, p, 13, color))
    user_label(e, 12, 1000, p)
    e.append(box("topbar", 240, 0, 1680, 64, p["bg"]))
    e.append(box("topbar-line", 240, 63, 1680, 1, p["line"]))
    breadcrumbs(e, active, p, leaf)
    return {"pageType": "content", "background": solid(p["bg"]), "elements": e}


def heading(page: dict, title: str, subtitle: str, p: dict[str, str], action: str | None = None) -> None:
    """Добавляет Header 2 и действие страницы. @param page Страница. @param title Заголовок. @param subtitle Описание. @param p Палитра. @param action Action Button. @return None."""
    e = page["elements"]
    e.append(txt("page-title", 264, 84, 1100, 32, title, p, 24, bold=True, line=28))
    if subtitle:
        e.append(txt("page-subtitle", 264, 120, 1200, 22, subtitle, p, 13, p["secondary"]))
    if action:
        button(e, "page-action", 1708, 84, 188, action, p, "action", "fas:plus")


def home(p: dict[str, str]) -> dict[str, Any]:
    """Создаёт главную текущего проекта. @param p Палитра. @return Страница с входами в функции и последними компиляциями."""
    page = shell(p, "Главная")
    e = page["elements"]
    heading(page, "Neuro Photo / Mobile", "Мобильное приложение генерации изображений", p)
    e.append(txt("functions-heading", 264, 176, 700, 28, "Функции проекта", p, 20, bold=True, line=24))
    functions = [("Тесты", "Сценарии, редактирование и компиляция UI-тестов.", "24 сценария", "fas:list-check"),
                 ("Контекст", "Элементы интерфейса, запросы, темы и разрешения.", "9 элементов", "fas:cubes"),
                 ("Интеграции", "Бэкенды генерации и публикация результатов в GitLab.", "GitLab · 3 бэкенда", "fas:plug")]
    for i, (name, desc, meta, icon_name) in enumerate(functions):
        x = 264 + i * 550
        card(e, f"function-card-{i}", x, 216, 530, 240, p)
        e.append(box(f"function-icon-bg-{i}", x + 20, 236, 40, 40, p["generic"], 8))
        e.append(ico(f"function-icon-{i}", x + 31, 247, 18, icon_name, p["secondary"]))
        e.append(ico(f"function-arrow-{i}", x + 474, 248, 16, "fas:arrow-right", p["secondary"]))
        e.append(txt(f"function-name-{i}", x + 20, 296, 470, 28, name, p, 20, bold=True, line=24))
        e.append(txt(f"function-desc-{i}", x + 20, 336, 470, 40, desc, p, 13, p["secondary"], wrap=True))
        e.append(box(f"function-line-{i}", x + 20, 396, 490, 1, p["line"]))
        e.append(txt(f"function-meta-{i}", x + 20, 412, 470, 24, meta, p, 13, p["secondary"]))
    e.append(txt("recent-heading", 264, 508, 700, 28, "Последние компиляции", p, 20, bold=True, line=24))
    e.append(box("recent-table-top", 264, 552, 1632, 1, p["line"]))
    e.append(box("recent-head-line", 264, 592, 1632, 1, p["line"]))
    cols = [("Тест", 264), ("Статус", 760), ("Артефакты", 1150), ("Завершена", 1500)]
    for name, x in cols:
        e.append(txt(f"recent-col-{name}", x, 553, 240, 40, name, p, 13, p["text"], bold=True))
    recent = [("AUTH_001", "Успешно", "4", "сегодня, 01:48", "success"),
              ("GEN_014", "Ошибка семантики", "0", "сегодня, 01:32", "danger"),
              ("AUTH_007", "Успешно", "4", "вчера, 16:05", "success")]
    for i, (test_id, status_value, artifacts, date, theme) in enumerate(recent):
        y = 593 + i * 40
        e.append(box(f"recent-line-{i}", 264, y + 39, 1632, 1, p["line"]))
        e.append(txt(f"recent-id-{i}", 264, y, 300, 40, test_id, p, 13, mono=True))
        label(e, f"recent-status-{i}", 760, y + 8, 174 if i == 1 else 112, status_value, p, theme)
        e.append(txt(f"recent-artifacts-{i}", 1150, y, 100, 40, artifacts, p, 13, p["secondary"]))
        e.append(txt(f"recent-date-{i}", 1500, y, 240, 40, date, p, 13, p["secondary"]))
    return page


def tests(p: dict[str, str]) -> dict[str, Any]:
    """Создаёт список тест-кейсов на компонентах Gravity. @param p Палитра. @return Страница."""
    page = shell(p, "Автотесты")
    e = page["elements"]
    heading(page, "Автотесты", "Neuro Photo / Mobile · 24 сценария", p)
    input_control(e, "test-search", 264, 172, 630, "Поиск по имени, ID или тегу", p, "fas:magnifying-glass", placeholder=True)
    input_control(e, "status-select", 910, 172, 176, "Все статусы", p, select=True)
    input_control(e, "platform-select", 1102, 172, 190, "Все платформы", p, select=True)
    input_control(e, "author-select", 1308, 172, 170, "Все авторы", p, select=True)
    button(e, "table-settings", 1494, 172, 180, "Настроить колонки", p, "normal", "fas:sliders")
    button(e, "reset", 1690, 172, 206, "Сбросить фильтры", p, "flat", "fas:rotate-right")
    label(e, "filter-error", 264, 224, 132, "С ошибками", p, "danger", close=True)
    label(e, "filter-android", 408, 224, 112, "Android", p, "normal", close=True)
    e.append(txt("found", 1700, 224, 196, 24, "Найдено: 7", p, 13, p["secondary"], align="right"))
    e.append(box("table-top", 264, 276, 1632, 1, p["line"]))
    e.append(box("table-head-line", 264, 316, 1632, 1, p["line"]))
    checkbox(e, "check-all", 281, 290, False, p, indeterminate=True)
    cols = [("Имя", 320, 216), ("ID", 552, 100), ("Фича", 668, 140), ("Команда", 824, 130),
            ("Теги", 970, 190), ("Статус", 1176, 120), ("Платформы", 1312, 104),
            ("Владелец", 1432, 60), ("Изменено", 1508, 190)]
    for name, x, w in cols:
        e.append(txt(f"test-col-{name}", x, 277, w, 40, name, p, 13, p["text"], bold=True))
    rows = [("Анонимный вход", "AUTH_001", "Авторизация", "Mobile Core", ["auth", "smoke"], "Активен", "success",
             [("fab:android", 18), ("fab:apple", 12)], "АН", "06.11.2026 01:48"),
            ("Генерация изображения", "GEN_014", "Генерация", "AI Generation", ["generation", "critical"], "Ошибка", "danger",
             [("fab:android", 18), ("fab:apple", 12)], "МА", "06.11.2026 01:32"),
            ("Проверка paywall", "PAY_008", "Монетизация", "Revenue", ["payments"], "Интеграция", "info",
             [("fab:android", 18), ("fab:apple", 12), ("fas:clipboard-list", 12)], "АН", "05.11.2026 18:20"),
            ("Вход по email", "AUTH_007", "Авторизация", "Mobile Core", ["auth"], "Набросок", "normal",
             [("fab:android", 18), ("fab:apple", 12)], "ИЛ", "05.11.2026 16:05"),
            ("Сохранение в галерею", "GEN_021", "Генерация", "AI Generation", ["generation"], "Сломан", "danger",
             [("fab:android", 18), ("fab:apple", 12)], "МА", "04.11.2026 12:40"),
            ("Ограничения анонима", "AUTH_012", "Авторизация", "Mobile Core", ["auth", "limits"], "Нестабилен", "warning",
             [("fab:android", 18), ("fab:apple", 12)], "АН", "03.11.2026 09:15"),
            ("Повторная генерация", "GEN_018", "Генерация", "AI Generation", ["regression"], "Активен", "success",
             [("fab:android", 18), ("fab:apple", 12)], "ИЛ", "01.11.2026 14:02")]
    for i, (name, test_id, feature, team, tags, status_value, theme, platforms, owner, date) in enumerate(rows):
        y = 317 + i * 40
        if i == 1:
            e.append(box(f"test-row-selected-{i}", 264, y, 1632, 40, p["selection"]))
        e.append(box(f"test-row-line-{i}", 264, y + 39, 1632, 1, p["line"]))
        checkbox(e, f"test-check-{i}", 281, y + 13, i == 1, p)
        e.append(txt(f"test-name-{i}", 320, y, 216, 40, name, p, 13))
        e.append(txt(f"test-id-{i}", 552, y, 100, 40, test_id, p, 13, p["secondary"], mono=True))
        e.append(txt(f"test-feature-{i}", 668, y, 140, 40, feature, p, 13, p["secondary"]))
        e.append(txt(f"test-team-{i}", 824, y, 130, 40, team, p, 13, p["secondary"]))
        tag_x = 970
        for j, tag in enumerate(tags):
            tag_w = max(40, round(len(tag) * 7.1 + 16))
            label(e, f"test-tag-{i}-{j}", tag_x, y + 10, tag_w, tag, p, "normal", size="xs")
            tag_x += tag_w + 8
        status_w = max(64, round(len(status_value) * 7.1 + 16))
        label(e, f"test-status-{i}", 1176, y + 10, status_w, status_value, p, theme, size="xs")
        platform_width = sum(width for _, width in platforms) + 8 * (len(platforms) - 1)
        platform_x = 1312 + (104 - platform_width) / 2
        for j, (icon_name, icon_w) in enumerate(platforms):
            platform = ico(f"test-platform-{i}-{j}", platform_x, y + 12, 16, icon_name, p["secondary"])
            platform["bounds"][2] = icon_w
            e.append(platform)
            platform_x += icon_w + 8
        e.append(box(f"test-owner-{i}", 1448, y + 6, 28, 28, p["misc"], 14, p["lineStrong"]))
        e.append(txt(f"test-owner-initials-{i}", 1448, y + 6, 28, 28, owner, p, 11, p["text"], align="center", line=16))
        e.append(txt(f"test-date-{i}", 1508, y, 190, 40, date, p, 13, p["secondary"]))
        icon_button(e, f"test-more-{i}", 1862, y + 6, "fas:ellipsis", p)
    e.append(txt("pagination-summary", 264, 620, 600, 28, "1–7 из 7", p, 13, p["secondary"]))
    pagination(e, "table-pagination", 1652, 620, p)
    return page


def test_description(p: dict[str, str]) -> dict[str, Any]:
    """Добавляет правую панель описания и комментариев выбранного тест-кейса. @param p Палитра. @return Страница списка с раскрытой вкладкой «Описание»."""
    page = tests(p)
    e = page["elements"]
    panel = box("test-details-panel", 1280, 0, 640, H, p["float"], border=p["lineStrong"])
    panel["shadow"] = {"blur": 24, "color": "#00000026", "offset": [-4, 0]}
    e.append(panel)
    panel_actions = [("expand", 1768), ("more", 1812), ("close", 1856)]
    for action, x in panel_actions:
        e.append(box(f"test-details-{action}-bg", x, 16, 28, 28, p["generic"], 8))
        icon_name = "ellipsis" if action == "more" else ("xmark" if action == "close" else "expand")
        e.append(image(f"test-details-{action}-icon", x + 6, 22, 16, 16,
                       f"media/gravity-{icon_name}-{p['theme']}.svg"))
    e.append(txt("test-details-title", 1320, 28, 460, 40, "Генерация изображения", p, 24, bold=True, line=28))
    e.append(txt("test-details-dates", 1320, 76, 540, 24,
                 "Создан 01.11.2026 14:20 · обновлён 06.11.2026 01:32", p, 13, p["secondary"]))
    e.append(txt("test-details-description", 1320, 116, 520, 44,
                 "Проверяет генерацию изображения по текстовому описанию, отображение результата и сохранение в галерею.",
                 p, 13, p["text"], wrap=True, line=20))
    e.append(box("test-details-tabs-line", 1320, 215, 560, 1, p["line"]))
    e.append(txt("test-details-tab-description", 1320, 176, 96, 40, "Описание", p, 15,
                 p["text"], align="center", line=20))
    e.append(box("test-details-tab-description-current", 1320, 214, 96, 2, p["brand"]))
    e.append(txt("test-details-tab-test", 1424, 176, 64, 40, "Тест", p, 15,
                 p["secondary"], align="center", line=20))
    parameters = [("Статус", "status"), ("Фича", "feature"), ("ID", "id"),
                  ("Команда", "team"), ("Теги", "tags"), ("Автор", "author")]
    parameter_value_x = 1456  # 136 px от заголовка: измерено по референсу Tracker.
    for i, (name, key) in enumerate(parameters):
        y = 240 + i * 36  # Шаг 36 px соответствует ритму строк референса.
        e.append(txt(f"test-detail-{key}-caption", 1320, y, 124, 36, name, p, 13, p["secondary"], bold=True))
        if key == "status":
            label(e, "test-detail-status-value", parameter_value_x, y + 6, 76, "Ошибка", p, "danger")
        elif key == "feature":
            e.append(txt("test-detail-feature-value", parameter_value_x, y, 340, 36, "Генерация", p, 13))
        elif key == "id":
            e.append(txt("test-detail-id-value", parameter_value_x, y, 340, 36, "GEN_014", p, 13, mono=True))
        elif key == "team":
            e.append(txt("test-detail-team-value", parameter_value_x, y, 340, 36, "AI Generation", p, 13))
        elif key == "tags":
            label(e, "test-detail-tag-generation", parameter_value_x, y + 8, 94, "generation", p, "normal", size="xs")
            label(e, "test-detail-tag-critical", parameter_value_x + 102, y + 8, 68, "critical", p, "normal", size="xs")
        else:
            e.append(box("test-detail-author-avatar", parameter_value_x, y + 4, 28, 28, p["misc"], 14, p["lineStrong"]))
            e.append(txt("test-detail-author-initials", parameter_value_x, y + 4, 28, 28, "МА", p, 11, align="center", line=16))
            e.append(txt("test-detail-author-name", parameter_value_x + 40, y, 260, 36, "Мария Андреева", p, 13))
    activity_tabs_y = 488  # 32 px от нижней границы последней строки описания.
    e.append(box("test-activity-tabs-line", 1320, activity_tabs_y + 39, 560, 1, p["line"]))
    e.append(txt("test-activity-tab-comments", 1320, activity_tabs_y, 112, 40, "Комментарии", p, 15,
                 p["text"], align="center", line=20))
    e.append(box("test-activity-tab-comments-current", 1320, activity_tabs_y + 38, 112, 2, p["brand"]))
    e.append(txt("test-activity-tab-attachments", 1440, activity_tabs_y, 88, 40, "Вложения", p, 15,
                 p["secondary"], align="center", line=20))
    e.append(txt("test-activity-tab-history", 1536, activity_tabs_y, 80, 40, "История", p, 15,
                 p["secondary"], align="center", line=20))
    comments = [
        ("МА", "Мария Андреева", 114, "06 ноя 2026",
         "Уточнила ожидаемое состояние галереи после сохранения результата."),
        ("ИС", "Илья Соколов", 95, "07 ноя 2026",
         "Добавь проверку ошибки генерации: сейчас сценарий покрывает только успешный результат."),
    ]
    composer_y = activity_tabs_y + 64  # 24 px от нижней линии Tabs до поля ввода.
    e.append(box("comment-composer", 1320, composer_y, 560, 144, p["bg"], 8, p["line"]))
    e.append(txt("comment-composer-placeholder", 1332, composer_y + 12, 536, 18,
                 "Ваши комментарии важны для системы", p, 13, p["hint"], line=18))
    composer_actions_y = composer_y + 104  # Button M: 28 px + нижний отступ 12 px.
    e.append(ico("comment-attachment-icon", 1332, composer_actions_y + 6, 16, "fas:paperclip", p["secondary"]))
    e.append(txt("comment-attachment-text", 1356, composer_actions_y, 156, 28,
                 "Прикрепить вложение", p, 13, p["secondary"], line=18))
    e.append(box("comment-send-bg", 1772, composer_actions_y, 96, 28, p["generic"], 8))
    e.append(txt("comment-send-text", 1784, composer_actions_y, 72, 28,
                 "Отправить", p, 13, p["hint"], align="center", line=18))
    comment_y = composer_y + 168  # 24 px между полем ввода и первым комментарием.
    for i, (initials, author, author_w, date, comment) in enumerate(comments):
        e.append(box(f"test-comment-{i}-avatar", 1320, comment_y, 32, 32, p["misc"], 16, p["lineStrong"]))
        e.append(txt(f"test-comment-{i}-initials", 1320, comment_y, 32, 32, initials, p, 11,
                     align="center", line=16))
        e.append(txt(f"test-comment-{i}-author", 1368, comment_y, author_w, 18, author, p, 13,
                     p["text"], bold=True, line=18))
        e.append(txt(f"test-comment-{i}-date", 1368 + author_w + 12, comment_y + 1, 80, 16,
                     date, p, 11, p["secondary"], line=16))
        comment_text_y = comment_y + 22  # Body 1: 18 px + системный интервал 4 px.
        e.append(txt(f"test-comment-{i}-text", 1368, comment_text_y, 472, 36, comment, p, 13,
                     p["text"], wrap=True, line=18))
        comment_y = comment_text_y + 60  # 36 px текста + 24 px между комментариями.
    return page


def test_details(p: dict[str, str]) -> dict[str, Any]:
    """Добавляет правую панель с просмотром YAML выбранного тест-кейса. @param p Палитра. @return Страница списка с раскрытой вкладкой «Тест»."""
    page = tests(p)
    e = page["elements"]
    panel = box("test-details-panel", 1280, 0, 640, H, p["float"], border=p["lineStrong"])
    panel["shadow"] = {"blur": 24, "color": "#00000026", "offset": [-4, 0]}
    e.append(panel)
    panel_actions = [("expand", 1768), ("more", 1812), ("close", 1856)]
    for action, x in panel_actions:
        e.append(box(f"test-details-{action}-bg", x, 16, 28, 28, p["generic"], 8))
        icon_name = "ellipsis" if action == "more" else ("xmark" if action == "close" else "expand")
        e.append(image(f"test-details-{action}-icon", x + 6, 22, 16, 16,
                       f"media/gravity-{icon_name}-{p['theme']}.svg"))
    e.append(txt("test-details-title", 1320, 28, 460, 40, "Генерация изображения", p, 24, bold=True, line=28))
    e.append(txt("test-details-dates", 1320, 76, 540, 24,
                 "Создан 01.11.2026 14:20 · обновлён 06.11.2026 01:32", p, 13, p["secondary"]))
    e.append(txt("test-details-description", 1320, 116, 520, 44,
                 "Проверяет генерацию изображения по текстовому описанию, отображение результата и сохранение в галерею.",
                 p, 13, p["text"], wrap=True, line=20))
    e.append(box("test-details-tabs-line", 1320, 215, 560, 1, p["line"]))
    e.append(txt("test-details-tab-description", 1320, 176, 96, 40, "Описание", p, 15,
                 p["secondary"], align="center", line=20))
    e.append(txt("test-details-tab-test", 1424, 176, 64, 40, "Тест", p, 15,
                 p["text"], align="center", line=20))
    e.append(box("test-details-tab-test-current", 1424, 214, 64, 2, p["brand"]))

    # Единственный блок вкладки: заголовок с представлениями и нередактируемый YAML.
    e.append(box("test-code-block", 1320, 240, 560, 800, p["float"], 8, p["line"]))
    e.append(box("test-code-body", 1321, 300, 558, 739, p["code"], 7))
    e.append(box("test-code-body-top", 1321, 300, 558, 8, p["code"]))
    e.append(box("test-code-header-line", 1320, 299, 560, 1, p["line"]))
    # Icon-only SegmentedRadioGroup L: ручной тест, YAML и no-code блоки.
    e.append(box("test-view-mode-border", 1336, 252, 144, 36, "#00000000", 8, p["line"]))
    e.append(box("test-view-mode-current", 1384, 252, 48, 36, p["selection"], 0, p["brand"]))
    for i, icon_name in enumerate(["person", "file-code", "cubes-3"]):
        state = "active" if i == 1 else "normal"
        e.append(image(f"test-view-mode-icon-{i}", 1336 + i * 48 + 16, 262, 16, 16,
                       f"media/gravity-{icon_name}-{state}-{p['theme']}.svg"))
    e.append(box("test-edit-bg", 1720, 252, 144, 36, p["generic"], 8))
    e.append(image("test-edit-icon", 1734, 262, 16, 16, f"media/gravity-pencil-{p['theme']}.svg"))
    e.append(txt("test-edit-text", 1758, 252, 92, 36, "Редактировать", p, 13, line=36))

    yaml_lines = [
        "test:",
        "  metadata:",
        '    id: "GEN_014"',
        '    name: "Генерация изображения"',
        "  steps:",
        "    - step:",
        '        stepDescription: "Открываем экран генерации"',
        "        actions:",
        "          - wait:",
        "              condition:",
        "                elementName: LinearScreen",
        "                elementProperty: visible",
        "                propertyValue: true",
        "              timeout: 5s",
        "    - step:",
        '        stepDescription: "Запускаем генерацию"',
        "        actions:",
        "          - tap:",
        "              elementName: LinearGenerate",
        "              tapType: single",
        "    - step:",
        '        stepDescription: "Проверяем результат"',
        "        actions:",
        "          - wait:",
        "              condition:",
        "                elementName: GeneratedImage",
        "                elementProperty: visible",
        "                propertyValue: true",
        "              timeout: 10s",
        "          - tap:",
        "              elementName: SaveButton",
        "              tapType: single",
        "          - wait:",
        "              condition:",
        "                elementName: GalleryImage",
        "                elementProperty: visible",
        "                propertyValue: true",
        "              timeout: 5s",
    ]
    for i, code in enumerate(yaml_lines):
        y = 316 + i * 18
        e.append(txt(f"test-yaml-number-{i}", 1336, y, 24, 18, str(i + 1), p, 11,
                     p["hint"], mono=True, align="right", line=18))
        color = p["infoText"] if code.lstrip().startswith("-") or code == "test:" else p["text"]
        e.append(txt(f"test-yaml-line-{i}", 1376, y, 472, 18, code, p, 11, color,
                     mono=True, line=18))
    return page


def test_manual(p: dict[str, str]) -> dict[str, Any]:
    """Создаёт человекочитаемое представление выбранного тест-кейса. @param p Палитра. @return Страница панели «Тест» с последовательностью ручных действий."""
    page = test_details(p)
    e = page["elements"]
    e[:] = [element for element in e if not element["elementId"].startswith(("test-yaml-number-", "test-yaml-line-"))]
    for element in e:
        element_id = element["elementId"]
        if element_id == "test-view-mode-border":
            element["border"]["color"] = p["brand"]
        elif element_id == "test-view-mode-current":
            element["bounds"] = [1336, 252, 48, 36]
            element["shapeName"] = "roundRect"
            element["adjustments"] = [radius_for(48, 36, 8)]
        elif element_id == "test-view-mode-icon-0":
            element["src"] = f"media/gravity-person-active-{p['theme']}.svg"
        elif element_id == "test-view-mode-icon-1":
            element["src"] = f"media/gravity-file-code-normal-{p['theme']}.svg"
    e.append(box("test-view-mode-current-square", 1376, 253, 8, 34, p["selection"]))
    e.append(connector("test-view-mode-current-border", 1384, 252, 1384, 288, p["brand"]))

    steps = [
        ("Открыть экран генерации", [
            ("Дождаться открытия экрана генерации",
             "Экран виден пользователю. Ожидание — не более 5 секунд."),
        ]),
        ("Запустить генерацию", [
            ("Нажать кнопку «Сгенерировать»",
             "Одно нажатие запускает генерацию изображения."),
        ]),
        ("Проверить и сохранить результат", [
            ("Дождаться появления изображения",
             "Результат генерации виден. Ожидание — не более 10 секунд."),
            ("Нажать кнопку «Сохранить»",
             "Сохранить сгенерированное изображение в галерею."),
            ("Проверить сохранение",
             "Сохранённое изображение отображается в галерее."),
        ]),
    ]
    step_positions = [324, 496, 668]
    for i, ((title, actions), y) in enumerate(zip(steps, step_positions)):
        if i < len(steps) - 1:
            e.append(connector(f"manual-step-link-{i}", 1350, y + 40, 1350, step_positions[i + 1], p["lineStrong"]))
        e.append(box(f"manual-step-{i}-marker", 1336, y, 28, 28, p["generic"], 14))
        e.append(txt(f"manual-step-{i}-number", 1336, y, 28, 28, str(i + 1), p, 13,
                     p["text"], bold=True, align="center", line=18))
        e.append(txt(f"manual-step-{i}-title", 1380, y - 2, 464, 32, title, p, 15,
                     p["text"], bold=True, line=20))
        for j, (action, detail) in enumerate(actions):
            action_y = y + 52 + j * 64
            e.append(box(f"manual-action-{i}-{j}-dot", 1383, action_y + 7, 6, 6, p["lineStrong"], 3))
            e.append(txt(f"manual-action-{i}-{j}-title", 1404, action_y, 440, 20,
                         action, p, 13, p["text"], line=18))
            e.append(txt(f"manual-action-{i}-{j}-detail", 1404, action_y + 24, 440, 36,
                         detail, p, 13, p["secondary"], wrap=True, line=18))
    return page


def settings(p: dict[str, str]) -> dict[str, Any]:
    """Создаёт настройки проекта на формах Gravity. @param p Палитра. @return Страница."""
    page = shell(p, "Настройки проекта")
    e = page["elements"]
    heading(page, "Настройки проекта", "Neuro Photo / Mobile · параметры компиляции и публикации.", p)
    button(e, "save", 1732, 84, 164, "Сохранить", p, "action", "fas:check")
    tabs(e, [("Общие", 264, 64, False), ("Бэкенды и GitLab", 356, 144, True),
             ("Команда", 528, 80, False), ("Безопасность", 636, 124, False)], 158, p)
    e.append(txt("backends-heading", 264, 228, 700, 28, "Генерация артефактов", p, 20, bold=True, line=24))
    e.append(txt("backends-copy", 264, 260, 860, 22, "Бэкенды получают IR после успешного семантического анализа.", p, 13, p["secondary"]))
    card(e, "backends-card", 264, 304, 920, 344, p, False)
    backends = [("Android · Kaspresso", "Исполняемый UI-тест · Kotlin", "fab:android", True, "v1.6"),
                ("iOS · KIF", "Исполняемый UI-тест · Swift", "fab:apple", True, "v3.8"),
                ("Allure TestOps", "Ручной тест и описание шагов", "fas:chart-pie", True, "v2.1"),
                ("Техническая документация", "Markdown из единого источника", "fas:file-lines", False, "—")]
    for i, (name, desc, icon_name, enabled, version) in enumerate(backends):
        y = 320 + i * 80
        if i:
            e.append(box(f"backend-line-{i}", 284, y - 4, 880, 1, p["line"]))
        e.append(ico(f"backend-icon-{i}", 288, y + 18, 18, icon_name, p["secondary"]))
        e.append(txt(f"backend-name-{i}", 324, y + 2, 480, 24, name, p, 13, bold=True))
        e.append(txt(f"backend-desc-{i}", 324, y + 28, 480, 20, desc, p, 13, p["secondary"]))
        e.append(txt(f"backend-version-{i}", 1000, y + 12, 70, 28, version, p, 11, p["secondary"], mono=True, align="right"))
        switch(e, f"backend-switch-{i}", 1120, y + 17, enabled, p)
    e.append(txt("gitlab-heading", 1220, 228, 600, 28, "Интеграция GitLab", p, 20, bold=True, line=24))
    e.append(txt("gitlab-copy", 1220, 260, 640, 22, "Публикация сгенерированных тестов через Merge Request.", p, 13, p["secondary"]))
    card(e, "gitlab-card", 1220, 304, 676, 480, p, False)
    e.append(ico("gitlab-icon", 1240, 324, 20, "fab:gitlab", p["brandHeavy"]))
    label(e, "gitlab-status", 1752, 322, 124, "Подключено", p, "success")
    field(e, "gitlab-url", 1240, 368, 636, "GitLab URL", "https://gitlab.company.ru", p, True)
    field(e, "gitlab-repo", 1240, 452, 636, "Проект", "mobile/neuro-photo-autotests", p, True)
    field(e, "gitlab-branch", 1240, 536, 300, "Целевая ветка", "develop", p, True)
    field(e, "gitlab-path", 1576, 536, 300, "Каталог", "autotests/generated", p, True)
    e.append(txt("auto-mr", 1240, 632, 460, 24, "Создавать Merge Request автоматически", p, 13, bold=True))
    switch(e, "auto-mr-switch", 1836, 635, True, p)
    e.append(txt("auto-mr-copy", 1240, 660, 540, 20, "Ветка: automaton/{test_id} · ревьюер: владелец теста", p, 11, p["secondary"], mono=True, line=16))
    e.append(box("gitlab-line", 1240, 704, 636, 1, p["line"]))
    e.append(txt("token", 1240, 724, 300, 24, "Project Access Token", p, 13, bold=True))
    e.append(txt("token-value", 1240, 750, 360, 20, "••••••••••••  ·  до 28.12.2026", p, 11, p["secondary"], mono=True, line=16))
    button(e, "check-gitlab", 1672, 724, 204, "Проверить связь", p, "normal", "fas:plug")
    e.append(txt("compile-heading", 264, 700, 700, 28, "Правила компиляции", p, 20, bold=True, line=24))
    card(e, "compile-card", 264, 744, 920, 172, p, False)
    e.append(txt("compiler-version", 288, 764, 360, 24, "Версия компилятора", p, 13, p["secondary"]))
    input_control(e, "compiler-select", 288, 796, 360, "0.4.0 · закреплена", p, select=True, mono=True)
    e.append(txt("warnings", 688, 764, 340, 24, "Останавливаться при предупреждениях", p, 13, bold=True))
    switch(e, "warnings-switch", 1120, 767, True, p)
    e.append(txt("deterministic", 688, 824, 340, 24, "Детерминированная сборка IR", p, 13, bold=True))
    switch(e, "deterministic-switch", 1120, 827, True, p)
    card(e, "jit-card", 1220, 808, 676, 108, p, False)
    e.append(txt("jit-title", 1240, 824, 440, 24, "JIT · динамическое исполнение", p, 13, p["secondary"], bold=True))
    e.append(txt("jit-copy", 1240, 852, 480, 20, "Недоступно в текущей версии компилятора", p, 11, p["hint"], line=16))
    label(e, "jit-label", 1752, 850, 124, "Концепт", p, "normal")
    return page


def service_settings(p: dict[str, str]) -> dict[str, Any]:
    """Создаёт глобальные настройки сервиса на Gravity Card и SegmentedRadioGroup. @param p Палитра. @return Страница."""
    page = shell(p, "Настройки")
    e = page["elements"]
    heading(page, "Настройки", "Глобальные параметры интерфейса Automaton.", p)
    card(e, "appearance-card", 264, 184, 800, 216, p, False)
    e.append(txt("appearance-title", 288, 208, 500, 24, "Интерфейс", p, 15, bold=True, line=20))
    e.append(txt("theme-caption", 288, 260, 500, 20, "Тема", p, 13, p["secondary"]))
    selected_theme = "Светлая" if p["theme"] == "light" else "Тёмная"
    segmented(e, "theme-segmented", 288, 292, 456, ["Системная", "Светлая", "Тёмная"], selected_theme, p)
    e.append(txt("theme-copy", 288, 348, 640, 20, "Выбранная тема используется во всех проектах.", p, 13, p["secondary"]))
    card(e, "service-card", 1096, 184, 800, 216, p, False)
    e.append(txt("service-title", 1120, 208, 500, 24, "О сервисе", p, 15, bold=True, line=20))
    e.append(txt("service-version-caption", 1120, 260, 300, 24, "Версия сервиса", p, 13, p["secondary"]))
    e.append(txt("service-version", 1572, 260, 300, 24, "v0.4.0", p, 13, mono=True, align="right"))
    e.append(box("service-version-line", 1120, 300, 752, 1, p["line"]))
    e.append(txt("service-type-caption", 1120, 320, 300, 24, "Тип сервиса", p, 13, p["secondary"]))
    e.append(txt("service-type", 1572, 320, 300, 24, "SaaS · веб", p, 13, align="right"))
    return page


CODE = ["compilationUnit:", "  context:", "    elements:", "      - element:", "          elementName: AuthScreen",
        "          elementLayout: $figma/auth", "      - element:", "          elementName: AnonymButton",
        "          elementLayout: $figma/auth", "    theme:", "      themeMode: light", "  test:", "    metadata:",
        '      id: "AUTH_001"', '      name: "Successful anonymous entry"', "    steps:", "      - step:",
        '          stepDescription: "Открываем экран авторизации"', "          actions:", "            - wait:",
        "                condition:", "                  elementName: AuthScreen", "                  elementProperty: visible",
        "                  propertyValue: true", "                timeout: 5s", "      - step:",
        '          stepDescription: "Входим анонимно"', "          actions:", "            - tap:", "                  elementName: AnonymButton"]


def editor_yaml(p: dict[str, str]) -> dict[str, Any]:
    """Создаёт YAML-редактор с Gravity-контролами. @param p Палитра. @return Страница."""
    page = shell(p, "Автотесты", "AUTH_001")
    e = page["elements"]
    e.append(txt("editor-title", 264, 84, 800, 32, "Анонимный вход", p, 24, bold=True, line=28))
    label(e, "saved", 540, 88, 106, "Сохранено", p, "success")
    button(e, "compile", 1684, 84, 212, "Скомпилировать", p, "action", "fas:play")
    segmented(e, "editor-mode", 264, 152, 280, ["YAML", "Блок-схема"], "YAML", p,
              ["fas:file-code", "fas:cubes"])
    e.append(txt("file-path", 576, 152, 640, 36, "tests/auth/AUTH_001.yaml", p, 13, p["secondary"], mono=True))
    card(e, "editor-card", 264, 208, 1632, 796, p, False)
    e.append(box("tree-bg", 264, 208, 224, 796, p["generic"]))
    e.append(box("tree-line", 487, 208, 1, 796, p["line"]))
    e.append(txt("tree-heading", 284, 228, 180, 20, "СТРУКТУРА", p, 11, p["hint"], bold=True, line=16))
    tree = [("AUTH_001", "fas:file-code", 0, True), ("Context", "fas:cubes", 1, False), ("Elements · 9", "fas:shapes", 2, False),
            ("Requests · 0", "fas:server", 2, False), ("Theme · light", "fas:sun", 2, False), ("Permissions · 2", "fas:shield-halved", 2, False),
            ("Test", "fas:list-check", 1, False), ("Metadata", "fas:tag", 2, False), ("Steps · 3", "fas:list-ol", 2, False)]
    for i, (name, icon_name, level, current) in enumerate(tree):
        y = 268 + i * 40
        if current:
            e.append(box("tree-current", 272, y, 208, 32, p["selection"], 6))
        e.append(ico(f"tree-icon-{i}", 284 + level * 12, y + 8, 16, icon_name, p["brandHeavy"] if current else p["secondary"]))
        e.append(txt(f"tree-text-{i}", 312 + level * 12, y, 150 - level * 12, 32, name, p, 13, p["brandHeavy"] if current else p["secondary"]))
    e.append(box("code-bg", 488, 208, 996, 796, p["code"]))
    e.append(box("code-head", 488, 208, 996, 40, p["float"]))
    e.append(txt("code-caption", 508, 208, 640, 40, "AUTH_001.yaml  ·  UTF-8  ·  YAML", p, 11, p["secondary"], mono=True, line=16))
    icon_button(e, "copy", 1442, 214, "far:copy", p)
    for i, code in enumerate(CODE):
        y = 256 + i * 22
        if i == 19:
            e.append(box("code-current", 488, y, 996, 22, p["selection"]))
        e.append(txt(f"code-no-{i}", 504, y, 36, 22, str(i + 1), p, 11, p["hint"], mono=True, align="right", line=16))
        color = p["infoText"] if code.lstrip().startswith(("-", "compilationUnit")) else p["text"]
        e.append(txt(f"code-line-{i}", 560, y, 884, 22, code, p, 13, color, mono=True, line=20))
    e.append(box("compile-panel", 1484, 208, 412, 796, p["float"]))
    e.append(box("compile-panel-line", 1483, 208, 1, 796, p["line"]))
    e.append(txt("compile-heading", 1508, 228, 220, 24, "Компиляция", p, 15, bold=True, line=20))
    label(e, "compile-label", 1764, 228, 112, "Без ошибок", p, "success")
    stages = [("Лексический анализ", "24 токена"), ("AST", "Дерево построено"), ("Семантика", "Ссылки разрешены"), ("IR", "Готово к генерации")]
    for i, (name, desc) in enumerate(stages):
        y = 288 + i * 72
        if i < 3:
            e.append(connector(f"stage-link-{i}", 1518, y + 24, 1518, y + 72, p["positiveText"], 1))
        e.append(box(f"stage-dot-{i}", 1510, y + 8, 16, 16, p["positive"], 8))
        e.append(ico(f"stage-check-{i}", 1514, y + 12, 8, "fas:check", p["positiveText"]))
        e.append(txt(f"stage-name-{i}", 1540, y, 240, 24, name, p, 13, bold=True))
        e.append(txt(f"stage-desc-{i}", 1540, y + 24, 260, 20, desc, p, 13, p["secondary"]))
    e.append(box("artifact-line", 1508, 600, 364, 1, p["line"]))
    e.append(txt("artifacts-heading", 1508, 624, 260, 24, "Артефакты", p, 15, bold=True, line=20))
    artifacts = [("Android / Kaspresso", "fab:android"), ("iOS / KIF", "fab:apple"), ("Allure TestOps", "fas:chart-pie")]
    for i, (name, icon_name) in enumerate(artifacts):
        y = 672 + i * 48
        e.append(ico(f"artifact-icon-{i}", 1508, y + 4, 16, icon_name, p["secondary"]))
        e.append(txt(f"artifact-name-{i}", 1538, y, 260, 24, name, p, 13))
        e.append(ico(f"artifact-ok-{i}", 1848, y + 4, 16, "fas:circle-check", p["positiveText"]))
    e.append(box("editor-status", 264, 976, 1632, 28, p["generic"]))
    e.append(txt("editor-status-text", 280, 976, 900, 28, "main  ·  компилятор 0.4.0  ·  грамматика загружена", p, 11, p["secondary"], mono=True, line=16))
    e.append(txt("editor-status-right", 1560, 976, 312, 28, "Ln 20, Col 19  ·  0 ошибок", p, 11, p["secondary"], mono=True, align="right", line=16))
    return page


def editor_blocks(p: dict[str, str]) -> dict[str, Any]:
    """Создаёт блочный редактор с Gravity-каркасом. @param p Палитра. @return Страница."""
    page = shell(p, "Автотесты", "AUTH_001")
    e = page["elements"]
    e.append(txt("editor-title", 264, 84, 800, 32, "Анонимный вход", p, 24, bold=True, line=28))
    label(e, "synced", 540, 88, 172, "Синхронизировано", p, "success")
    button(e, "compile", 1684, 84, 212, "Скомпилировать", p, "action", "fas:play")
    segmented(e, "editor-mode", 264, 152, 280, ["YAML", "Блок-схема"], "Блок-схема", p,
              ["fas:file-code", "fas:cubes"])
    e.append(txt("sync-copy", 576, 152, 740, 36, "Изменения блоков обновляют исходный YAML", p, 13, p["secondary"]))
    card(e, "editor-card", 264, 208, 1632, 796, p, False)
    e.append(box("steps-bg", 264, 208, 232, 796, p["generic"]))
    e.append(box("steps-line", 495, 208, 1, 796, p["line"]))
    e.append(txt("steps-heading", 284, 228, 180, 20, "ШАГИ ТЕСТА", p, 11, p["hint"], bold=True, line=16))
    steps = [("01", "Открыть авторизацию", "1 действие"), ("02", "Войти анонимно", "1 действие"), ("03", "Проверить главную", "4 действия")]
    for i, (num, name, count) in enumerate(steps):
        y = 276 + i * 88
        if i == 1:
            e.append(box("step-current", 272, y - 8, 216, 76, p["selection"], 6))
        e.append(txt(f"step-num-{i}", 284, y, 28, 20, num, p, 11, p["brandHeavy"] if i == 1 else p["hint"], mono=True, line=16))
        e.append(txt(f"step-name-{i}", 320, y - 4, 148, 40, name, p, 13, p["brandHeavy"] if i == 1 else p["text"], bold=True, wrap=True))
        e.append(txt(f"step-count-{i}", 320, y + 38, 140, 18, count, p, 11, p["secondary"], line=16))
    button(e, "add-step", 284, 580, 192, "Добавить шаг", p, "normal", "fas:plus")
    e.append(box("canvas", 496, 208, 1032, 796, p["code"]))
    for i in range(24):
        e.append(box(f"grid-v-{i}", 516 + i * 42, 228, 1, 736, p["grid"]))
    for i in range(17):
        e.append(box(f"grid-h-{i}", 516, 240 + i * 42, 992, 1, p["grid"]))
    e.append(txt("canvas-caption", 524, 226, 360, 24, "TEST · AUTH_001", p, 11, p["secondary"], mono=True, line=16))
    nodes = [("start", 770, 280, 360, 64, "НАЧАЛО ТЕСТА", "Successful anonymous entry", "fas:play", "success"),
             ("step1", 690, 392, 520, 96, "ШАГ 01", "Открываем экран авторизации", "fas:list-ol", "normal"),
             ("wait", 744, 528, 412, 64, "WAIT · 5s", "AuthScreen.visible = true", "fas:clock", "info"),
             ("step2", 690, 640, 520, 96, "ШАГ 02", "Нажимаем на вход анонимным пользователем", "fas:list-ol", "selected"),
             ("tap", 744, 776, 412, 64, "TAP · single", "AnonymButton", "fas:hand-pointer", "normal")]
    for nid, x, y, w, h, kicker, value, icon_name, theme in nodes:
        selected = theme == "selected"
        card(e, nid + "-card", x, y, w, h, p, False, selected)
        if theme in ("success", "info"):
            e.append(box(nid + "-semantic", x, y, 4, h, p["positiveText"] if theme == "success" else p["infoText"], 2))
        e.append(ico(nid + "-icon", x + 16, y + 16, 16, icon_name, p["brandHeavy"] if selected else p["secondary"]))
        e.append(txt(nid + "-kicker", x + 44, y + 8, w - 60, 18, kicker, p, 11, p["brandHeavy"] if selected else p["secondary"], mono=True, line=16))
        e.append(txt(nid + "-value", x + 44, y + 28, w - 60, h - 32, value, p, 13, bold=True, wrap=h > 80))
        e.append(box(nid + "-port", x + w / 2 - 5, y + h - 5, 10, 10, p["float"], 5, p["lineStrong"]))
    for i, (y1, y2) in enumerate([(344, 392), (488, 528), (592, 640), (736, 776)]):
        e.append(connector(f"node-link-{i}", 950, y1, 950, y2, p["brand"] if i == 2 else p["lineStrong"], 2, True))
    e.append(box("zoom", 1356, 932, 148, 36, p["float"], 8, p["line"]))
    icon_button(e, "zoom-minus", 1362, 936, "fas:minus", p)
    e.append(txt("zoom-value", 1396, 932, 58, 36, "100%", p, 11, p["secondary"], mono=True, align="center", line=16))
    icon_button(e, "zoom-plus", 1468, 936, "fas:plus", p)
    e.append(box("properties", 1528, 208, 368, 796, p["float"]))
    e.append(box("properties-line", 1527, 208, 1, 796, p["line"]))
    e.append(txt("properties-heading", 1552, 228, 280, 24, "Свойства шага", p, 15, bold=True, line=20))
    field(e, "property-name", 1552, 276, 320, "Описание", "Входим анонимно", p)
    e.append(txt("action-heading", 1552, 364, 280, 20, "ДЕЙСТВИЯ · 1", p, 11, p["hint"], bold=True, line=16))
    card(e, "action-card", 1552, 400, 320, 116, p, False)
    e.append(ico("action-icon", 1572, 420, 16, "fas:hand-pointer", p["brandHeavy"]))
    e.append(txt("action-title", 1600, 412, 220, 24, "Tap", p, 13, bold=True, mono=True))
    e.append(txt("action-key", 1572, 452, 260, 18, "elementName", p, 11, p["secondary"], mono=True, line=16))
    e.append(txt("action-value", 1572, 476, 260, 20, "AnonymButton", p, 13, p["infoText"], mono=True))
    field(e, "tap-type", 1552, 548, 320, "Тип нажатия", "single", p, True, select=True)
    alert(e, "semantic-alert", 1552, 652, 320, "Семантика проверена",
          "AnonymButton объявлен в Context и поддерживает tap.", p)
    button(e, "delete-step", 1552, 916, 320, "Удалить шаг", p, "outlined-danger", "fas:trash")
    return page


def robot(elements: list, p: dict[str, str], x: float, y: float, state: str, scale: float = 1.0) -> None:
    """Вставляет фирменного автомата-марионетку. @param elements Список. @param p Палитра. @param x,y Позиция. @param state healthy, partial или critical. @param scale Масштаб. @return None."""
    size = 640 * scale
    elements.append(image("robot-character", x, y, size, size, f"media/automaton-{state}.png"))


def status(p: dict[str, str], state: str = "partial") -> dict[str, Any]:
    """Создаёт сцену суммарного состояния SaaS. @param p Палитра. @param state Состояние. @return Страница."""
    page = shell(p, "Состояние системы")
    e = page["elements"]
    stage = box("status-stage", 240, 64, 1680, 1016, p["bg"])
    stage["fill"] = {"type": "gradient", "gradientType": "linear", "angle": 25,
                     "stops": [{"position": 0, "color": "#F4F0F2" if p["theme"] == "light" else "#171317"},
                               {"position": 1, "color": p["bg"]}]}
    e.append(stage)
    glow_alpha = {"healthy": "14", "partial": "26", "critical": "40"}[state]
    glow = box("status-ambient", 300, 128, 1040, 864, "#00000000")
    glow["fill"] = {"type": "gradient", "gradientType": "radial",
                    "stops": [{"position": 0, "color": p["brand"] + glow_alpha},
                              {"position": 0.62, "color": p["brand"] + "0A"},
                              {"position": 1, "color": p["brand"] + "00"}]}
    e.append(glow)
    for i, thread_x in enumerate([492, 612, 754, 906, 1068, 1170]):
        e.append(connector(f"ambient-thread-{i}", thread_x, 64, thread_x + (i % 2) * 12, 824,
                           p["brand"] + ("26" if p["theme"] == "light" else "40"), 1))
    for i in range(7):
        e.append(connector(f"ambient-scan-{i}", 288, 704 + i * 34, 1252, 704 + i * 34,
                           p["brand"] + "14" if i % 2 else p["line"], 1))
    veil = box("diagnostics-veil", 1180, 64, 740, 872, "#00000000")
    veil["fill"] = {"type": "gradient", "gradientType": "linear", "angle": 0,
                    "stops": [{"position": 0, "color": p["bg"] + "00"},
                              {"position": 0.34, "color": p["bg"] + "E6"},
                              {"position": 1, "color": p["bg"]}]}
    e.append(veil)
    e.append(txt("page-title", 264, 84, 1100, 32, "Состояние системы", p, 24, bold=True, line=28))
    e.append(txt("page-subtitle", 264, 120, 1200, 22, "Automaton SaaS · обновлено только что", p, 13, p["secondary"]))
    button(e, "refresh", 1732, 84, 164, "Обновить", p, "normal", "fas:rotate-right")
    data = {
        "healthy": ("Все системы работают", "Автоматон работает",
                    "Единый контур генерации работает штатно. Все шесть компонентов доступны.", "success", "6 из 6 компонентов"),
        "partial": ("Частичная деградация", "Автоматон нестабилен",
                    "Генерация iOS недоступна, а GitLab отвечает с задержками. Остальной контур продолжает работать.", "warning", "4 из 6 компонентов"),
        "critical": ("Критический сбой", "Автоматон сломан",
                     "Генерация остановлена. Доступны только Frontend и AST Module, остальные компоненты не отвечают.", "danger", "2 из 6 компонентов"),
    }
    status_label, title, subtitle, theme, summary = data[state]
    label(e, "global-state-label", 288, 158, 232, status_label, p, theme)
    e.append(txt("global-state-title", 288, 196, 940, 56, title, p, 40, bold=True, line=48))
    e.append(txt("global-state-copy", 288, 256, 780, 44, subtitle, p, 15, p["secondary"], wrap=True, line=20))
    robot(e, p, 404, 292, state, 1.1875)
    bottom_fade = box("status-bottom-fade", 240, 844, 1052, 236, "#00000000")
    bottom_fade["fill"] = {"type": "gradient", "gradientType": "linear", "angle": 90,
                           "stops": [{"position": 0, "color": p["bg"] + "00"},
                                     {"position": 0.72, "color": p["bg"] + "E6"},
                                     {"position": 1, "color": p["bg"]}]}
    e.append(bottom_fade)
    e.append(txt("components-kicker", 1320, 164, 520, 20, "ПРИЧИНЫ СОСТОЯНИЯ", p, 11, p["hint"], bold=True, line=16))
    e.append(txt("components-heading", 1320, 194, 520, 28, summary, p, 20, bold=True, line=24))
    e.append(txt("components-copy", 1320, 226, 520, 20, "Сводная диагностика · проверка каждые 30 секунд", p, 13, p["secondary"]))
    e.append(box("components-top-line", 1320, 270, 552, 1, p["line"]))
    components = [("Frontend", "Приём YAML и REST-запросов"), ("AST Module", "Построение синтаксического дерева"),
                  ("Semantic Module", "Контекст и семантические проверки"), ("Android Backend", "Kaspresso · генератор Kotlin"),
                  ("iOS Backend", "KIF · генератор Swift"), ("GitLab Integration", "Ветки и Merge Requests")]
    for i, (name, desc) in enumerate(components):
        if state == "healthy":
            item_theme, value = "success", "Работает"
        elif state == "partial":
            item_theme, value = ("danger", "Недоступен") if i == 4 else (("warning", "Задержки") if i == 5 else ("success", "Работает"))
        else:
            item_theme, value = ("success", "Работает") if i in (0, 1) else ("danger", "Недоступен")
        y = 296 + i * 82
        if i:
            e.append(box(f"component-line-{i}", 1320, y - 10, 552, 1, p["line"]))
        e.append(txt(f"component-name-{i}", 1320, y, 310, 22, name, p, 13, bold=True, mono=True))
        e.append(txt(f"component-desc-{i}", 1320, y + 24, 360, 18, desc, p, 11, p["secondary"], line=16))
        label(e, f"component-label-{i}", 1740, y + 6, 132, value, p, item_theme)
    e.append(box("metrics-line", 264, 936, 1632, 1, p["line"]))
    metrics = [("99,97%", "Доступность за 30 дней"), ("184 мс", "Медианный ответ API"),
               ("30 сек.", "Интервал проверки"), ("0", "Активных инцидентов" if state == "healthy" else "Решённых инцидентов за сутки")]
    for i, (value, caption) in enumerate(metrics):
        x = 288 + i * 408
        e.append(txt(f"metric-value-{i}", x, 956, 180, 28, value, p, 20, bold=True, mono=True, line=24))
        e.append(txt(f"metric-caption-{i}", x, 988, 320, 20, caption, p, 13, p["secondary"]))
    return page


def save_page(name: str, page: dict[str, Any]) -> None:
    """Сохраняет страницу. @param name Имя файла. @param page Содержимое. @return None."""
    path = ROOT / "pages" / name
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(yaml.safe_dump(page, allow_unicode=True, sort_keys=False, width=1000), encoding="utf-8")


def main() -> None:
    """Генерирует набор Automaton SaaS. @return None. Перезаписывает основной .kanva.pptd и 21 страницу."""
    pages = [("01_home_light.page", home(LIGHT)), ("02_home_dark.page", home(DARK)),
             ("03_tests_light.page", tests(LIGHT)), ("04_tests_dark.page", tests(DARK)),
             ("05_test_details_light.page", test_details(LIGHT)), ("06_test_details_dark.page", test_details(DARK)),
             ("07_settings_light.page", settings(LIGHT)), ("08_settings_dark.page", settings(DARK)),
             ("09_editor_yaml_light.page", editor_yaml(LIGHT)), ("10_editor_blocks_dark.page", editor_blocks(DARK)),
             ("11_status_light.page", status(LIGHT, "partial")), ("12_status_dark.page", status(DARK, "partial")),
             ("13_automaton_healthy.page", status(LIGHT, "healthy")),
             ("14_automaton_partial.page", status(DARK, "partial")),
             ("15_automaton_critical.page", status(LIGHT, "critical")),
             ("16_service_settings_light.page", service_settings(LIGHT)),
             ("17_service_settings_dark.page", service_settings(DARK)),
             ("18_test_description_light.page", test_description(LIGHT)),
             ("19_test_description_dark.page", test_description(DARK)),
             ("20_test_manual_light.page", test_manual(LIGHT)),
             ("21_test_manual_dark.page", test_manual(DARK))]
    for name, page in pages:
        save_page(name, page)
    deck = {"version": "v2", "title": "Automaton SaaS · Gravity UI · Full HD", "size": [W, H],
            "customFonts": [{"family": "Inter", "src": "https://fonts.googleapis.com/css2?family=Inter"}],
            "theme": {"colors": {"brand": LIGHT["brand"], "text": LIGHT["text"], "background": LIGHT["bg"]}},
            "pages": [f"pages/{name}" for name, _ in pages]}
    (ROOT / "automaton-saas.kanva.pptd").write_text(yaml.safe_dump(deck, allow_unicode=True, sort_keys=False, width=1000), encoding="utf-8")


if __name__ == "__main__":
    main()
