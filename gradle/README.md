# FinanControl (App de Finanzas)

## Integrantes
- Carlos Meza

## Descripción
Aplicación Android desarrollada en Kotlin para registrar ingresos y gastos, visualizar historial y mantener persistencia local.

## Funcionalidades implementadas
- Pantalla principal con resumen (Balance)
- Historial de movimientos (RecyclerView)
- Registro de movimientos (Ingreso/Gasto) con validaciones
- Persistencia local con Room (los datos se mantienen al cerrar la app)
- Formato CLP sin decimales ($ 1.234.567)

## Tecnologías
- Kotlin
- Material 3 (Theme)
- Room (SQLite)
- MVVM (ViewModel + Repository)
- RecyclerView

## Cómo ejecutar
1. Clonar el repositorio:
    - `git clone https://github.com/TU_USUARIO/FinanControl.git`
2. Abrir el proyecto en Android Studio
3. Esperar Gradle Sync
4. Ejecutar en un emulador o dispositivo (minSdk 24)

## Estructura del proyecto
- `data/` (Room, modelos, repositorio)
- `viewmodel/` (ViewModel)
- `ui/` (Activities, adapter)
- `utils/` (formateo de dinero)
