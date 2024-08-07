import { IPersonType } from 'app/entities/person-type/person-type.model';
import { IConference } from 'app/entities/conference/conference.model';
import { IUser } from 'app/entities/user/user.model';

export interface IApplicationUser {
  id: number;
  phoneNumber?: string | null;
  persontype?: IPersonType | null;
  person?: IConference | null;
  internalUser?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
