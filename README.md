# JHAML COMPILER

This compiler will compile [.HAML](https://haml.info/tutorial.html) files to usable [.HTML](https://www.w3schools.com/html/) files.

## Installation

Clone repository to locale.
        
        git clone (link)

## Usage

Use the compiler in therminal as follows:

    ### One-to-one Mode
        Compile a *.haml to the therminal 
                
                java -jar HamlIt.jar <input.haml>

    ### Many-to-many Mode
        Compile index.haml to index.html
                
                java -jar HamlIt.jar index.haml:index.html
                
        Compiles index.jhaml and contact.jhml to index.html and contact.html

                java -jar HamlIt.jar index.haml:index.html contact.haml:contact.html

        Compiles all haml files in themes/ to html files in public/site/.

                java -jar HamlIt.jar themes:public/site

##Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
Syntra-mvl-team-beige