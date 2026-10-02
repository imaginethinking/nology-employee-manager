export interface Address {
    addressLine1: string;
    addressLine2: string | null;
    city: string;
    postcode: string;
    country: string;
}

export interface ContactDetails {
    emailAddress: string;
    mobileNumber: string;
    address: Address;
}

export interface Employee {
    id: string;
    firstName: string;
    middleName: string | null;
    lastName: string;
    contactDetails: ContactDetails;
}

export type ActiveFilter = 'all' | 'true' | 'false';
