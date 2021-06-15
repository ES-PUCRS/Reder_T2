export class MenuItem {
    name: string = "";
    operation: (() => void) = () => {};
    dropdownOpen: boolean = false;
    dropdown: Array<MenuItem> = [];
    
}