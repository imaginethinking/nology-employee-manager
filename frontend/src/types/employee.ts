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

export interface EmployeeRequest {
    firstName: string;
    middleName?: string;
    lastName: string;
    contactDetails: {
        emailAddress: string;
        mobileNumber: string;
        address: {
            addressLine1: string;
            addressLine2?: string;
            city: string;
            postcode: string;
            country: string;
        };
    };
}

export type ActiveFilter = 'all' | 'true' | 'false';

export interface EmployeeQueryParams {
    page: number;
    size: number;
    search: string;
    active: ActiveFilter;
}
