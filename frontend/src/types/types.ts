// Interface for the users table
export interface User {
    id: string;
    name: string;
    username: string;
    email: string;
}

// Interface for the professional_profiles table
export interface ProfessionalProfile {
    id: number;
    profession: string;
    specialty: string;
    experience: string;
    userId: number; // Foreign key referencing users(id)
}

// Interface for the study_cases table
export interface Lead {
    id: number;
    title: string;
    description: string;
    createdAt: Date;
    userId: number;
}

export interface StudyCaseInput {
    title: string;
    description?: string;
  }

  export interface UpdateStudyCaseInput {
    id: string;
    title?: string;
    description?: string;
  }

// Interface for the roles table
/* export interface Role {
    id: number;
    name: string;
} */


export interface Collaborator {
    id: number;
    userId: number;
    studyCaseId: number;
    //role_id: number;
}


export interface Document {
    id: number;
    name: string;
    fileType: string | null;
    content: string;
    urlPath: string;
    studyCaseId: number; // Foreign key referencing study_cases(id)
}

// Interface for the chats table
export interface Chat {
    id: number;
    studyCaseId: number; // Foreign key referencing study_cases(id)
}

// Interface for the messages table
export interface Message {
    id: number;
    content: string;
    createdAt: Date;
    chatId: number; // Foreign key referencing chats(id)
}

// Interface for the invitations table
export interface Invitation {
    id: number;
    status: string; // CHAR(1) NOT NULL
    senderId: number; // Foreign key referencing users(id)
    receiverId: number; // Foreign key referencing users(id)
}
