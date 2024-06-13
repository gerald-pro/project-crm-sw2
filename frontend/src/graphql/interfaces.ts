export interface LeadInput {
  title: string;
  description?: string;
  userId: number;
  contactId: number;
  value: number;
  status?: string;
}

export interface Lead {
  id: string;
  title: string;
  description?: string;
  userId: string;
  contactId: string;
  value: number;
  status: string;
  createdAt: string;
}

export interface Contact {
  id: string;
  name: string;
  email: string;
  phone?: string;
  address?: string;
  createdAt?: string;
}

export interface ContactInput {
  name: string;
  email: string;
  phone?: string;
  address?: string;
}

export interface ContactsPage {
  getContacts: {
    contacts: Contact[];
    totalCount: number;
    totalPages: number;
  };
}