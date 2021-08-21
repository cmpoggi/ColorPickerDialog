# ColorPickerDialog
Java class for android to display a color selector dialog which I wrote for my own projects which I decided to release as open source.

## Screenshots
![gridpicker](https://user-images.githubusercontent.com/14188843/130328704-401a00c0-f19e-4be6-a998-5918918e17b2.jpg) ![circlepicker](https://user-images.githubusercontent.com/14188843/130328716-3ed423c8-b69f-4271-87fc-cadea496e7cf.jpg)



## How to use
Just add the file in you android project.

To create the dialog:
```java
ColorPickerDialog colorPickerDialog = new ColorPickerDialog(
                    context,
                    (int)initial_color,
                    "title of dialog",
                    "text for OK button",
                    "text for cancel button",
                    this::textView.setColor); //listener method it will pass the selected color as int
```

If you want to allow to use both color pickers you can add a "more" button
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
