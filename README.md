# ColorPickerDialog
Java class for android to display a color selector dialog which I wrote for my own projects which I decided to release as open source.

## Screenshots
![GridPicker](https://user-images.githubusercontent.com/14188843/130324178-35ced818-e863-4b5c-b765-972d43c2833e.png) ![CirclePicker](https://user-images.githubusercontent.com/14188843/130324191-a55d7768-b219-49cb-9831-073070fe0052.png)

## How to use
Just add the file in you android project.

To create the dialog you must use:

```java
ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
                    context,
                    (int)initial_color,
                    "title of dialog",
                    "text for OK button",
                    "text for cancel button",
                    "text for More button",
                    this::textView.setColor); //listener method it will pass the selected color as int
```

You can show either the grid picker dialog or circle picker by using:

```java
colorPickerDialog.showGridPicker();

colorPickerDialog.showCirclePicker();
```

## Changelog

Version 1.0
* Initial version
