# ColorPickerDialog
Java class for android to display a color selector dialog which I wrote for my own projects which I decided to release as open source.

## Screenshots
![gridpicker](https://user-images.githubusercontent.com/14188843/130324264-c45e5ecd-4cb2-4a19-bbff-7a229b2e57d1.jpg) ![circlepicker](https://user-images.githubusercontent.com/14188843/130324270-c971176d-3815-42f7-a580-4d62cac37859.jpg)


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
