export interface LeadInput {
  title: string;
  description?: string;
  userId: string;
  contactId: string;
  value: number;
  status?: string;
}

export interface UpdateLeadInput {
  id: String;
  title?: string;
  description?: string;
  userId?: string;
  contactId?: string;
  value?: number;
  status?: string;
}


export interface Lead {
  id: string;
  title: string;
  description?: string;
  userId: string;
  contactId: string;
  value: number;
  status: LeadStatus;
  createdAt: string;
}

export enum LeadStatus {
  nuevo = 'nuevo',
  calificado = 'calificado',
  en_curso = 'en_curso',
  oportunidad = 'oportunidad',
  completado = 'completado',
  cancelado = 'cancelado',
}


export interface Activity {
  id: string;
  title: string;
  description?: string;
  type: ActivityType;
  contactId: string;
  location?: number;
  activityDate: string;
  leadId: string;
}

export interface ActivityInput {
  title: string;
  description?: string;
  type: ActivityType;
  contactId: string;
  location?: number;
  activityDate: string;
  leadId: string;
}

export interface UpdateActivityInput {
  id: string;
  title?: string;
  description?: string;
  type?: ActivityType;
  contactId?: string;
  location?: number;
  activityDate?: string;
}

export enum ActivityType {
  CALL = 'CALL',
  MEETING = 'MEETING',
  LUNCH = 'LUNCH'
}

export interface User {
  id: string;
  name: string;
  username: string;
  email: string;
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