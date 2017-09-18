# Java Multimedia Retrieval Android

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![License: CC BY-NC 4.0](https://licensebuttons.net/l/by-nc/4.0/80x15.png)](https://creativecommons.org/licenses/by-nc/4.0/)

Este proyecto busca permitir al usuario realizar consultar a un sistema de recuperación de imágenes, visualizando las imágenes resultantes de dichas consultas de una manera atractiva, interactiva y amigable. En esta ocasión dicho sistema ha sido desarrollado para plataformas móviles, concretamente Android.

Cuando hablamos de un sistema de recuperación de información, \textit{CBIR}, nos referimos a los sistemas basados fundamentalmente en descriptores de bajo nivel (color, textura, etc.) obtenidos directamente a partir de la imagen. En ellos la idea es, mediante una imagen consulta, comprobar como de parecidas son el resto, imágenes resultado, y presentar los resultados

Al tratarse de una plataforma móvil hay que tener muy en cuenta los recursos que va a requerir dicho sistema, por lo que hay que esta especialmente atentos a ellos, ya que si el sistema necesita demasiados recursos puede funcionar incorrectamente y provocar incluso malfunciomaniento del propio teléfono.

Se busca interactividad por parte del usuario, por ello será capaz de moverse a través de las imágenes, tanto consulta como resultado, realizando movimientos de scroll. A su vez, se ha añadido mecanismos de ayuda, para que el usuario sepa en cada momento en que lugar se encuentra, ya que el resultado de una consulta puede ser de cientos de imágenes. Por otro lado, será capaz de modificar ciertos parámetros, como descriptor asociado, número de imágenes, para adecuar el uso del sistema a su experiencia deseada.

Todo lo descrito se va a desarrollado a partir de un CBIR concreto, [Java Multimedia Retrieval©](https://github.com/jesuschamorro/JMR).

## Licencias

### Código
[GNU General Public License v3.0](LICENSE)

### Memoria
[Creative Commons Attribution-NonCommercial 4.0 International](https://creativecommons.org/licenses/by-nc/4.0/)
