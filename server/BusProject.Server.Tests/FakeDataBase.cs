namespace BusProject.Server.Model
{
    using BusProject.Server.Model;
    using System;
    using System.Collections.Generic;

    public class FakeDataBase : IFileDataBase
    {

        public FakeDataBase()
        {
        }

        public void AddFile(string id, double length, DateTime time)
        {
        }

        public void DeleteFile(string id)
        {
            throw new NotImplementedException();
        }

        public bool FileExist(string id)
        {
            return false;
        }

        public List<ServerFile> GetFiles()
        {
            return new List<ServerFile>() { new ServerFile("test.txt",35000,DateTime.Today),
                                            new ServerFile("test2.mp3",4000,DateTime.Now)};
        }

        public string GetPath()
        {
            throw new NotImplementedException();
        }
    }
}
